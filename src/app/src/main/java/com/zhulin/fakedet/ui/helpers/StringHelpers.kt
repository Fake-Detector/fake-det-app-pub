package com.zhulin.fakedet.ui.helpers

object StringHelpers {
    fun String.claimUpdate(maxLength: Int = 21): String {
        return this
            .filter { it.isLetterOrDigit() || it in "@?_" }
            .replace("\\s+".toRegex(), "").let {
            if (it.length <= maxLength) it else it.substring(0, maxLength)
        }
    }

    fun String.claimValidate(minLength: Int = 3, maxLength: Int = 21): Boolean {
        return this.length in minLength..maxLength
    }
}