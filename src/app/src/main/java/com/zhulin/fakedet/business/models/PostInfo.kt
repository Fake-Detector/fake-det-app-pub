package com.zhulin.fakedet.business.models

import java.time.LocalDateTime

data class PostInfo (
    val id: Long,
    val externalId: String,
    val text: String,
    val mediaCount: Int,
    val trustValue: Int,
    val checked: Boolean,
    val dateTime: LocalDateTime
)