package com.zhulin.fakedet.business.models

data class FeatureInfo(
    val id: Long,
    val type: FeatureItemType,
    val textValue: String = "",
    val numberValue: Int = 0,
    val linkValue: LinkInfos = LinkInfos(),
    val aiValue: AiInfos = AiInfos(),
)
