package com.zhulin.fakedet.business.models

enum class Language(val language: String) {
    RU("ru"),
    EN("en");

    companion object {
        infix fun from(language: String): Language = entries.firstOrNull { it.language == language } ?: RU
    }
}