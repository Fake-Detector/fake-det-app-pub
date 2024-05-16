package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.models.AiInfos
import com.zhulin.fakedet.business.models.LinkInfos
import com.zhulin.fakedet.ui.theme.AppBlack

@Composable
fun ImageItemScreen(
    imageUrl: String,
    transcribe: String,
    trustValue: Int,
    aiGenerated: AiInfos,
    mood: String,
    tags: List<String>,
    links: LinkInfos,
    isChecked: Boolean,
    getString: (id: Int) -> String
) {
    ItemScreen(
        content = {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.logo_transparent)
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

@Preview
@Composable
private fun ImageItemScreenPreview() {
    val stringMap = mapOf(
        R.string.create_create_request to stringResource(id = R.string.create_create_request),
        R.string.post_transcribe to stringResource(id = R.string.post_transcribe),
        R.string.post_confidence_level to stringResource(id = R.string.post_confidence_level),
        R.string.post_ai_generated to stringResource(id = R.string.post_ai_generated),
        R.string.post_topic to stringResource(id = R.string.post_topic),
        R.string.post_mood to stringResource(id = R.string.post_mood),
        R.string.post_confidence_level to stringResource(id = R.string.post_confidence_level),
        R.string.post_links to stringResource(id = R.string.post_links),
        R.string.post_trusted to stringResource(id = R.string.post_trusted),
        R.string.post_untrusted to stringResource(id = R.string.post_untrusted),
    )

    ImageItemScreen(
        imageUrl = "https://images.ctfassets.net/hrltx12pl8hq/1fR5Y7KaK9puRmCDaIof7j/09e2b2b9eaf42d450aba695056793607/vector1.jpg?fit=fill&w=1200&h=630",
        transcribe = "LOREM IPSUM",
        trustValue = 50,
        aiGenerated = AiInfos(),
        mood = "Positive",
        tags = listOf(
            "AI",
            "IT"
        ),
        links = LinkInfos(),
        isChecked = false,
        getString = { id -> stringMap[id]!! }
    )
}