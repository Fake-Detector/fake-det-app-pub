package com.zhulin.fakedet.data.helpers

import com.zhulin.fakedet.business.models.AiInfos
import com.zhulin.fakedet.business.models.FeatureItemType
import com.zhulin.fakedet.business.models.ItemInfo
import com.zhulin.fakedet.business.models.ItemPostType
import com.zhulin.fakedet.business.models.LinkInfos
import com.zhulin.fakedet.data.helpers.FeatureHelpers.getInfo
import fake.detection.post.bridge.contracts.PostOuterClass
import fake.detection.post.bridge.contracts.PostOuterClass.FeatureType
import fake.detection.post.bridge.contracts.PostOuterClass.Item

object ItemHelpers {

    fun Item.getInfo() = ItemInfo(
        id = this.id,
        type = this.getItemPostType(),
        features = this.featuresList.map { it.getInfo() },
        text = this.data,
        url = this.data,
        isChecked = this.featuresList.any { it.type == FeatureType.Trust },
        trustValue = this.getTrust()
    )

    private fun Item.getItemPostType() = when (this.type) {
        PostOuterClass.ItemType.Text -> ItemPostType.TEXT
        PostOuterClass.ItemType.Image -> ItemPostType.PHOTO
        PostOuterClass.ItemType.ImageUrl -> ItemPostType.PHOTO
        PostOuterClass.ItemType.Audio -> ItemPostType.AUDIO
        PostOuterClass.ItemType.AudioUrl -> ItemPostType.AUDIO
        PostOuterClass.ItemType.Video -> ItemPostType.VIDEO
        PostOuterClass.ItemType.VideoUrl -> ItemPostType.VIDEO
        PostOuterClass.ItemType.UNRECOGNIZED -> ItemPostType.NONE
        else -> ItemPostType.NONE
    }

    fun Item.getTrust() =
        (this.featuresList.firstOrNull { it.type == FeatureType.Trust }?.text?.toIntOrNull() ?: 0)

    fun ItemInfo.getAiTrust() =
        this.features.firstOrNull { it.type == FeatureItemType.AI_GENERATED }?.aiValue ?: AiInfos()

    fun ItemInfo.getTags() =
        this.features.filter { it.type == FeatureItemType.TAG }.map { it.textValue }

    fun ItemInfo.getMood() =
        this.features.firstOrNull { it.type == FeatureItemType.MOOD }?.textValue ?: ""

    fun ItemInfo.getLinks() =
        this.features.firstOrNull { it.type == FeatureItemType.LINK }?.linkValue ?: LinkInfos()

    fun ItemInfo.getTranscription() =
        this.features.firstOrNull { it.type == FeatureItemType.TRANSCRIPTION && it.textValue.isNotBlank() }?.textValue ?: ""
}