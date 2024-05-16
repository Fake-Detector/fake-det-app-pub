package com.zhulin.fakedet.ui.screens.post.components

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.models.AiInfos
import com.zhulin.fakedet.business.models.LinkInfos
import com.zhulin.fakedet.ui.theme.AppBlack

@Composable
fun MediaItemScreen(
    mediaUrl: String,
    transcribe: String,
    trustValue: Int,
    aiGenerated: AiInfos,
    mood: String,
    tags: List<String>,
    links: LinkInfos,
    isChecked: Boolean,
    getString: (id: Int) -> String
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(mediaUrl)))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    ItemScreen(
        content = {
            Column {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .aspectRatio(0.5625f)
                )

                if (transcribe.isNotBlank()) {
                    Text(
                        text = getString(R.string.post_transcribe),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 24.sp,
                            fontSize = 24.sp,
                            color = AppBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Left,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    val textExpanded = remember { mutableStateOf(true) }
                    Text(
                        text = transcribe,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp,
                            fontSize = 20.sp,
                            color = AppBlack,
                            lineBreak = LineBreak.Paragraph.copy(strategy = LineBreak.Strategy.Balanced),
                            hyphens = Hyphens.Auto
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                            .clickable {
                                textExpanded.value = !textExpanded.value
                            },
                        maxLines = if (textExpanded.value) 10 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left
                    )
                }

            }

        },
        trustValue = trustValue,
        aiGenerated = aiGenerated,
        mood = mood,
        tags = tags,
        links = links,
        isChecked = isChecked,
        getString = getString
    )
}