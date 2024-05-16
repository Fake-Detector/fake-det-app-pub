package com.zhulin.fakedet.business.usecases

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.zhulin.fakedet.business.models.ContentInfo
import com.zhulin.fakedet.business.models.ContentType

class ContentHandler(
    private val context: Context
) {
    fun getContent(uri: Uri): ContentInfo? {
        val mimeType = context.contentResolver.getType(uri) ?: return null
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: return null
        var type = ContentType.NONE

        if (mimeType.startsWith("image/")) {
            type = ContentType.PHOTO
        } else if (mimeType.startsWith("video/")) {
            type = ContentType.VIDEO
        } else if (mimeType.startsWith("audio/")) {
            type = ContentType.AUDIO
        }

        if (type == ContentType.NONE) return null

        return ContentInfo(type, extension, uri)
    }

    fun getBytes(uri: Uri): ByteArray? = try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        }
    } catch (e: Exception) {
        null
    }

}