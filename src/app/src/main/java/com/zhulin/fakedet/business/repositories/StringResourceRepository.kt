package com.zhulin.fakedet.business.repositories

import com.zhulin.fakedet.business.models.Language

interface StringResourceRepository {
    fun get(id: Int, language: Language): String
}