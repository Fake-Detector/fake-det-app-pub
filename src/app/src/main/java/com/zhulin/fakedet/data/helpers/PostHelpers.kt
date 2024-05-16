package com.zhulin.fakedet.data.helpers

import com.zhulin.fakedet.business.models.PostFullInfo
import com.zhulin.fakedet.business.models.PostInfo
import com.zhulin.fakedet.data.helpers.ItemHelpers.getInfo
import com.zhulin.fakedet.data.helpers.ItemHelpers.getTrust
import fake.detection.post.bridge.contracts.PostOuterClass.FeatureType
import fake.detection.post.bridge.contracts.PostOuterClass.ItemType
import fake.detection.post.bridge.contracts.PostOuterClass.Post
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

object PostHelpers {
    fun Post.getInfo() = PostInfo(
        id = this.id,
        externalId = this.externalId,
        text = this.itemsList.firstOrNull { it.type == ItemType.Text }?.data ?: "",
        mediaCount = this.itemsList.count { it.type != ItemType.Text },
        trustValue = this.getTrust(),
        checked = this.getChecked(),
        dateTime = this.getTime()
    )

    fun Post.getFullInfo() = PostFullInfo(
        id = this.id,
        externalId = this.externalId,
        title = "",
        items = this.itemsList.map { it.getInfo() },
        trustValue = this.getTrust(),
        dateTime = this.getTime()
    )

    private fun Post.getTrust() =
        (this.itemsList.firstOrNull { it.type == ItemType.Text }
            ?: this.itemsList.firstOrNull())?.getTrust() ?: 0

    private fun Post.getChecked() =
        (this.itemsList.firstOrNull { it.type == ItemType.Text }
            ?: this.itemsList.firstOrNull())?.featuresList?.any { feature -> feature.type == FeatureType.Trust }
            ?: false

    private fun Post.getTime() =
        Instant.ofEpochSecond(this.createdAt.seconds, this.createdAt.nanos.toLong())
            .atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
}