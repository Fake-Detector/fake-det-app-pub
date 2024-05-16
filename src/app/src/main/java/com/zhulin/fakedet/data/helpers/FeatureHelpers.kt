package com.zhulin.fakedet.data.helpers

import com.google.gson.Gson
import com.zhulin.fakedet.business.models.AiInfos
import com.zhulin.fakedet.business.models.FeatureInfo
import com.zhulin.fakedet.business.models.FeatureItemType
import com.zhulin.fakedet.business.models.LinkInfos
import fake.detection.post.bridge.contracts.PostOuterClass.Feature
import fake.detection.post.bridge.contracts.PostOuterClass.FeatureType

object FeatureHelpers {
    fun Feature.getInfo() = FeatureInfo(
        id = this.id,
        type = this.getFeatureItemType(),
        textValue = this.getTextValue(),
        numberValue = this.getNumberValue(),
        linkValue = this.getLinkInfos(),
        aiValue = this.getAiInfos()
    )

    private fun Feature.getFeatureItemType() = when (this.type) {
        FeatureType.Trust -> FeatureItemType.TRUST
        FeatureType.Mood -> FeatureItemType.MOOD
        FeatureType.Tag -> FeatureItemType.TAG
        FeatureType.AiGenerated -> FeatureItemType.AI_GENERATED
        FeatureType.Link -> FeatureItemType.LINK
        FeatureType.Transcription -> FeatureItemType.TRANSCRIPTION
        FeatureType.UNRECOGNIZED -> FeatureItemType.NONE
        else -> FeatureItemType.NONE
    }

    private fun Feature.getTextValue() = when (this.getFeatureItemType()) {
        FeatureItemType.TAG -> this.text
        FeatureItemType.TRANSCRIPTION -> this.text
        FeatureItemType.MOOD -> this.text
        else -> ""
    }

    private fun Feature.getNumberValue() = when (this.getFeatureItemType()) {
        FeatureItemType.TRUST -> this.text.toIntOrNull() ?: 0
        FeatureItemType.AI_GENERATED -> this.text.toIntOrNull() ?: 0
        else -> 0
    }

    private fun Feature.getLinkInfos() = when (this.getFeatureItemType()) {
        FeatureItemType.LINK -> {
            try {
                val x = Gson().fromJson(this.text, LinkInfos::class.java)
                x
            } catch (error: Exception) {
                LinkInfos()
            }
        }

        else -> LinkInfos()
    }

    private fun Feature.getAiInfos() = when (this.getFeatureItemType()) {
        FeatureItemType.AI_GENERATED -> {
            try {
                val x = Gson().fromJson(this.text, AiInfos::class.java)
                x
            } catch (error: Exception) {
                AiInfos()
            }
        }

        else -> AiInfos()
    }
}