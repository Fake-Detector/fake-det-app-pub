package com.zhulin.fakedet.business.helpers

import com.zhulin.fakedet.business.models.Keyword
import com.zhulin.fakedet.business.models.KeywordsComparing

object LinkInfoHelpers {
    fun KeywordsComparing.getAllKeys(): Set<String> {
        val keysOriginal = this.original.map { it.key }.toSet()
        val keysCompare = this.compare.map { it.key }.toSet()

        return keysOriginal.plus(keysCompare)
    }

    fun List<Keyword>.getKeywords(key: String): List<String> {
        return this.filter { it.key.equals(key, true) }.flatMap { it.values }
    }

    fun KeywordsComparing.getRelevant(): Float {
        val keysOriginal = this.original.map { it.key }.toSet()
        var result = 0f

        for (key in keysOriginal) {
            val originalCount =
                this.original.filter { it.key.equals(key, true) }.flatMap { it.values }.size
            val intersectCount =
                this.intersection.filter { it.key.equals(key, true) }.flatMap { it.values }.size

            result += intersectCount * 1f / originalCount
        }

        return if (keysOriginal.isEmpty()) 0f else result / keysOriginal.size
    }
}