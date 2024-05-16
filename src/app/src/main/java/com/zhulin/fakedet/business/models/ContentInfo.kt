package com.zhulin.fakedet.business.models

import android.net.Uri

data class ContentInfo(
    val type: ContentType,
    val extension: String,
    val uri: Uri,
)
