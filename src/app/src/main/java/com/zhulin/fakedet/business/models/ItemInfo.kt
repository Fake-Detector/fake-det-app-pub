package com.zhulin.fakedet.business.models

data class ItemInfo(
    val id: String,
    val type: ItemPostType,
    val text: String = "",
    val url: String = "",
    val trustValue: Int,
    val isChecked: Boolean,
    val features: List<FeatureInfo>
)