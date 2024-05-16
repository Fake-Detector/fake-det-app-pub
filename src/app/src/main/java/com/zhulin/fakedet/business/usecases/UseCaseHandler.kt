package com.zhulin.fakedet.business.usecases

import javax.inject.Inject

data class UseCaseHandler @Inject constructor(
    val getStringResource : StringResourceHandler,
    val auth: AuthHandler,
    val changeLanguage: LanguageHandler,
    val userHandler: UserHandler,
    val postHandler: PostHandler,
    val contentHandler: ContentHandler,
)