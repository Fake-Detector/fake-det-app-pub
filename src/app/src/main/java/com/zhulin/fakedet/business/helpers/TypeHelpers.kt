package com.zhulin.fakedet.business.helpers

import com.zhulin.fakedet.business.models.ContentType
import fake.detection.post.bridge.contracts.PostOuterClass.ItemType


object TypeHelpers {
    fun ContentType.toItemType() = when (this) {
        ContentType.PHOTO -> ItemType.Image
        ContentType.VIDEO -> ItemType.Video
        ContentType.AUDIO -> ItemType.Audio
        ContentType.NONE -> ItemType.UNRECOGNIZED
    }
}