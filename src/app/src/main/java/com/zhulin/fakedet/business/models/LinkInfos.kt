package com.zhulin.fakedet.business.models

data class LinkInfos(
    val result: List<LinkInfo> = emptyList()
)

data class LinkInfo(
    val url: String,
    val semanticSimilarity: Double?,
    val textComparison: List<DiffComparison>,
    val keywordComparison: KeywordsComparing?,
    val originalText: String?,
    val originalTitle: String?
)

data class DiffComparison(
    val compareType: Int,
    val value: String
)

data class KeywordsComparing(
    val original: List<Keyword>,
    val compare: List<Keyword>,
    val intersection: List<Keyword>
)

data class Keyword(
    val key: String,
    val values: List<String>
)