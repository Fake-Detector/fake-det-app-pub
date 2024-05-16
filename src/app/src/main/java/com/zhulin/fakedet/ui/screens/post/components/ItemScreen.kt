package com.zhulin.fakedet.ui.screens.post.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.models.AiInfos
import com.zhulin.fakedet.business.models.LinkInfos
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getCompareCircleColor
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getTrustBackgroundColor
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getTrustCircleColor
import com.zhulin.fakedet.ui.screens.common.FullScreenDialog
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppPurple
import com.zhulin.fakedet.ui.theme.AppWhite

@Composable
fun ItemScreen(
    content: @Composable () -> Unit,
    trustValue: Int = -1,
    mood: String = "",
    aiGenerated: AiInfos = AiInfos(),
    tags: List<String> = emptyList(),
    links: LinkInfos = LinkInfos(),
    isChecked: Boolean = false,
    getString: (id: Int) -> String = { "" }
) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 5.dp)
        ) {
            items(tags) {
                AssistChip(
                    onClick = { /*TODO*/ },
                    label = {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp,
                                fontSize = 16.sp,
                                color = AppWhite
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            textAlign = TextAlign.Left,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(containerColor = AppPurple),
                    shape = RoundedCornerShape(50.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(topStartPercent = 15, topEndPercent = 15))
                .background(if (isChecked) trustValue.getTrustBackgroundColor() else AppWhite)
                .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 10.dp)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    content()
                }

                item {
                    Text(
                        text = getString(R.string.post_result),
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
                }

                if (isChecked) {

                    item {
                        Text(
                            text = getString(if (trustValue > 50) R.string.post_trusted else R.string.post_untrusted),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                lineHeight = 20.sp,
                                fontSize = 20.sp,
                                color = AppBlack
                            ),
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Left,
                        )
                    }
                }

                item {
                    Column {
                        if (mood.isNotBlank()) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(getString(R.string.post_mood))
                                    }

                                    append(mood)
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 20.sp,
                                    fontSize = 20.sp,
                                    color = AppBlack
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Left,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Spacer(modifier = Modifier.height(5.dp))
                        }


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isChecked) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val color = trustValue.getTrustCircleColor()

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(75.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            progress = {
                                                maxOf(
                                                    0.1f,
                                                    (trustValue.toFloat() / 100)
                                                )
                                            },
                                            modifier = Modifier.size(75.dp),
                                            color = color,
                                            strokeWidth = 5.dp,
                                            trackColor = AppGray.copy(alpha = 0.5f),
                                        )
                                        Text(
                                            text = "$trustValue %",
                                            color = color,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                lineHeight = 14.sp,
                                                fontSize = 18.sp
                                            )
                                        )
                                    }

                                    Text(
                                        text = getString(R.string.post_confidence_level),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 20.sp,
                                            fontSize = 20.sp,
                                            color = color,
                                            lineBreak = LineBreak.Paragraph.copy(strategy = LineBreak.Strategy.Balanced),
                                            hyphens = Hyphens.Auto
                                        ),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(20.dp))
                            if (aiGenerated.overallHumanMade != -1.0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val aiGeneratedPercentage = aiGenerated.overallHumanMade .toInt()
                                    val color = aiGeneratedPercentage.getTrustCircleColor()

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(75.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            progress = {
                                                maxOf(
                                                    0.1f,
                                                    (aiGenerated.overallHumanMade / 100).toFloat()
                                                )
                                            },
                                            modifier = Modifier.size(75.dp),
                                            color = color,
                                            strokeWidth = 5.dp,
                                            trackColor = AppGray.copy(alpha = 0.5f),
                                        )
                                        Text(
                                            text = "$aiGeneratedPercentage %",
                                            color = color,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                lineHeight = 14.sp,
                                                fontSize = 18.sp
                                            )
                                        )
                                    }

                                    Text(
                                        text = getString(R.string.humand_made_text),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 20.sp,
                                            fontSize = 20.sp,
                                            color = color,
                                            lineBreak = LineBreak.Paragraph.copy(strategy = LineBreak.Strategy.Balanced),
                                            hyphens = Hyphens.Auto
                                        ),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }

                        }

                        if (aiGenerated.overallHumanMade != -1.0) {
                            for (sentence in aiGenerated.sentences) {
                                Spacer(modifier = Modifier.height(5.dp))

                                AiItemCustom(
                                    sentence = sentence.sentence,
                                    humanMade = sentence.humanMade,
                                    getString = getString
                                )

                                Spacer(modifier = Modifier.height(5.dp))
                            }


                        }


                        if (links.result.isNotEmpty()) {
                            Text(
                                text = getString(R.string.post_links),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 20.sp,
                                    fontSize = 20.sp,
                                    color = AppBlack
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Left,
                            )

                            val context = LocalContext.current
                            links.result.filter { it.url.isNotBlank() }
                                .forEachIndexed { index, link ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        ClickableText(
                                            text = buildAnnotatedString {
                                                withStyle(
                                                    style = SpanStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 24.sp,
                                                        color = AppBlack
                                                    )
                                                ) {
                                                    append("${index + 1}. ")
                                                }

                                                withStyle(
                                                    style = SpanStyle(
                                                        textDecoration = TextDecoration.Underline,
                                                        color = AppBlue
                                                    )
                                                ) {
                                                    append(link.url)
                                                }
                                            },
                                            onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(link.url)
                                                )
                                                context.startActivity(intent)
                                            },
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Normal,
                                                lineHeight = 18.sp,
                                                fontSize = 18.sp,
                                                color = AppBlack
                                            ),
                                            modifier = Modifier.weight(2f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )


                                        if (link.originalText != null) {
                                            val visibleMedia =
                                                remember { mutableStateOf(false) }
                                            Button(
                                                onClick = { visibleMedia.value = true },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = AppBlue
                                                ),
                                                modifier = Modifier
                                                    .widthIn(min = 200.dp)
                                                    .weight(1f)
                                            ) {
                                                Text(
                                                    text = getString(R.string.link_view),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        lineHeight = 14.sp,
                                                        fontSize = 14.sp,
                                                        color = AppWhite
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    textAlign = TextAlign.Center
                                                )
                                            }

                                            if (visibleMedia.value) {
                                                FullScreenDialog(
                                                    content = {
                                                        LinkItemCustom(
                                                            link,
                                                            onUrlClick = { url ->
                                                                val intent = Intent(
                                                                    Intent.ACTION_VIEW,
                                                                    Uri.parse(url)
                                                                )
                                                                context.startActivity(intent)
                                                            }, getString = getString
                                                        )
                                                    },
                                                    title = link.originalTitle,
                                                    onDismissRequest = {
                                                        visibleMedia.value = false
                                                    },
                                                )
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
            Surface(modifier = Modifier.weight(1f)) {

            }
            HorizontalDivider(thickness = 1.dp, color = AppBlack)
        }
    }

}