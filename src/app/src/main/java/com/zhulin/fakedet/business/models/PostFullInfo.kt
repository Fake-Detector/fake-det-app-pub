package com.zhulin.fakedet.business.models

import java.time.LocalDateTime

data class PostFullInfo(
    val id: Long,
    val externalId: String,
    val title: String,
    val items: List<ItemInfo>,
    val trustValue: Int,
    val dateTime: LocalDateTime,
)

