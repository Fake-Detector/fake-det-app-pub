package com.zhulin.fakedet.business.models

data class AiInfos(
    val overallHumanMade: Double = -1.0,
    val sentences: List<SententeceAiInfo> = emptyList()
)

data class SententeceAiInfo(
    val humanMade: Double,
    val sentence: String
)
