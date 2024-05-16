package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.helpers.LinkInfoHelpers.getAllKeys
import com.zhulin.fakedet.business.helpers.LinkInfoHelpers.getKeywords
import com.zhulin.fakedet.business.helpers.LinkInfoHelpers.getRelevant
import com.zhulin.fakedet.business.models.DiffComparison
import com.zhulin.fakedet.business.models.Keyword
import com.zhulin.fakedet.business.models.KeywordsComparing
import com.zhulin.fakedet.business.models.LinkInfo
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getCompareCircleColor
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getTrustCircleColor
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlackGreen
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppLightGreen
import com.zhulin.fakedet.ui.theme.AppRed
import com.zhulin.fakedet.ui.theme.AppWhite

@Composable
fun LinkItemCustom(
    linkInfo: LinkInfo,
    onUrlClick: (url: String) -> Unit,
    getString: (id: Int) -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {


        if (linkInfo.originalText != null) {
            Column {
                val originalTextExpanded = remember { mutableStateOf(false) }

                Text(
                    text = getString(R.string.link_content),
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

                Text(
                    text = linkInfo.originalText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        fontSize = 20.sp,
                        color = AppBlack,
                        lineBreak = LineBreak.Paragraph.copy(strategy = LineBreak.Strategy.Balanced),
                        hyphens = Hyphens.Auto
                    ),
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .clickable {
                            originalTextExpanded.value = !originalTextExpanded.value
                        }
                        .fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    maxLines = if (!originalTextExpanded.value) 4 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }



        if (linkInfo.textComparison.isNotEmpty()) {
            Column {
                val compareTextExpanded = remember { mutableStateOf(false) }

                Text(
                    text = getString(R.string.link_compare),
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

                Text(
                    text = buildAnnotatedString {
                        linkInfo.textComparison.forEach { diffComparison ->
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                when (diffComparison.compareType) {
                                    -1 -> append("-")
                                    1 -> append("+")
                                    else -> append("")
                                }
                            }
                            withStyle(
                                style = SpanStyle(
                                    background = when (diffComparison.compareType) {
                                        -1 -> AppRed
                                        0 -> AppWhite
                                        1 -> AppBlackGreen
                                        else -> AppGray
                                    }
                                )
                            ) {

                                append(diffComparison.value)
                            }
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        lineHeight = 20.sp,
                        fontSize = 20.sp,
                        color = AppBlack,
                        textAlign = TextAlign.Justify,
                        lineBreak = LineBreak.Paragraph.copy(strategy = LineBreak.Strategy.Balanced),
                        hyphens = Hyphens.Auto
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, AppBlack)
                        .padding(5.dp)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .clickable {
                            compareTextExpanded.value = !compareTextExpanded.value
                        },
                    maxLines = if (!compareTextExpanded.value) 4 else Int.MAX_VALUE,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }

        if (linkInfo.keywordComparison != null) {
            Column {
                Text(
                    text = getString(R.string.link_keywords),
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

                val keywordKeys = linkInfo.keywordComparison.getAllKeys()


                Row(
                    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
                ) {
                    GridCell(content = {
                        Text(
                            text = getString(R.string.link_keyword_name),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp,
                                fontSize = 16.sp,
                                color = AppBlack
                            ),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }, modifier = Modifier.weight(1f))
                    GridCell(content = {
                        Text(
                            text = getString(R.string.link_keyword_original),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp,
                                fontSize = 16.sp,
                                color = AppBlack
                            ),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }, modifier = Modifier.weight(1f))
                    GridCell(content = {
                        Text(
                            text = getString(R.string.link_keyword_compare),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp,
                                fontSize = 16.sp,
                                color = AppBlack
                            ),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }, modifier = Modifier.weight(1f))
                    GridCell(content = {
                        Text(
                            text = getString(R.string.link_keyword_intersection),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp,
                                fontSize = 16.sp,
                                color = AppBlack
                            ),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }, modifier = Modifier.weight(1f))
                }

                for (key in keywordKeys) {
                    Row(
                        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
                    ) {
                        val keyTextExpanded = remember { mutableStateOf(false) }
                        val originalTextExpanded = remember { mutableStateOf(false) }
                        val articleTextExpanded = remember { mutableStateOf(false) }
                        val intersectTextExpanded = remember { mutableStateOf(false) }

                        GridCell(content = {
                            Text(
                                text = key,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 16.sp,
                                    fontSize = 16.sp,
                                    color = AppBlack
                                ),
                                modifier = Modifier
                                    .animateContentSize(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                    .clickable {
                                        keyTextExpanded.value = !keyTextExpanded.value
                                    }
                                    .padding(2.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = if (!keyTextExpanded.value) 1 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis
                            )
                        }, modifier = Modifier.weight(1f))
                        GridCell(content = {
                            Text(
                                text = linkInfo.keywordComparison.original.getKeywords(key)
                                    .joinToString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 16.sp,
                                    fontSize = 16.sp,
                                    color = AppBlack
                                ),
                                modifier = Modifier
                                    .animateContentSize(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                    .clickable {
                                        originalTextExpanded.value = !originalTextExpanded.value
                                    }
                                    .padding(2.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = if (!originalTextExpanded.value) 1 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis
                            )
                        }, modifier = Modifier.weight(1f))
                        GridCell(content = {
                            Text(
                                text = linkInfo.keywordComparison.compare.getKeywords(key)
                                    .joinToString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 16.sp,
                                    fontSize = 16.sp,
                                    color = AppBlack
                                ),
                                modifier = Modifier
                                    .animateContentSize(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                    .clickable {
                                        articleTextExpanded.value = !articleTextExpanded.value
                                    }
                                    .padding(2.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = if (!articleTextExpanded.value) 1 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis
                            )
                        }, modifier = Modifier.weight(1f))
                        GridCell(content = {
                            Text(
                                text = linkInfo.keywordComparison.intersection.getKeywords(key)
                                    .joinToString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 16.sp,
                                    fontSize = 16.sp,
                                    color = AppBlack
                                ),
                                modifier = Modifier
                                    .animateContentSize(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                    .clickable {
                                        intersectTextExpanded.value =
                                            !intersectTextExpanded.value
                                    }
                                    .padding(2.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = if (!intersectTextExpanded.value) 1 else Int.MAX_VALUE,
                                overflow = TextOverflow.Ellipsis
                            )
                        }, modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }

        if (linkInfo.semanticSimilarity != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val color = (linkInfo.semanticSimilarity * 100).toInt().getTrustCircleColor()

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(75.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { maxOf(0.1f, linkInfo.semanticSimilarity.toFloat()) },
                            modifier = Modifier.size(75.dp),
                            color = color,
                            strokeWidth = 5.dp,
                            trackColor = AppGray.copy(alpha = 0.5f),
                        )
                        Text(
                            text = "${(linkInfo.semanticSimilarity * 100).toInt()} %",
                            color = color,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 14.sp,
                                fontSize = 18.sp
                            )
                        )
                    }

                    Text(
                        text = getString(R.string.link_semantic_sim),
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

                if (linkInfo.keywordComparison != null) {
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val relevantCoefficient = linkInfo.keywordComparison.getRelevant()
                        val relevantPercentage =
                            (linkInfo.keywordComparison.getRelevant() * 100).toInt()
                        val color = relevantPercentage.getCompareCircleColor()

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(75.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = {
                                    maxOf(0.1f, relevantCoefficient)
                                },
                                modifier = Modifier.size(75.dp),
                                color = color,
                                strokeWidth = 5.dp,
                                trackColor = AppGray.copy(alpha = 0.5f),
                            )
                            Text(
                                text = "$relevantPercentage %",
                                color = color,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 14.sp,
                                    fontSize = 18.sp
                                )
                            )
                        }

                        Text(
                            text = getString(R.string.link_relevant_sim),
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
            Spacer(modifier = Modifier.height(15.dp))
        }

        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = AppBlack
                    )
                ) {
                    append(getString(R.string.link_original))
                }

                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = AppBlue
                    )
                ) {
                    append(linkInfo.url)
                }
            },
            onClick = { onUrlClick(linkInfo.url) },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                fontSize = 18.sp,
                color = AppBlack
            ),
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GridCell(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight(1f)
            .border(1.dp, AppBlack)
            .background(AppLightGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        content()
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun LinkItemCustomPreview() {
    val stringMap = mapOf(
        R.string.link_original to stringResource(id = R.string.link_original),
        R.string.link_content to stringResource(id = R.string.link_content),
        R.string.link_compare to stringResource(id = R.string.link_compare),
        R.string.link_keywords to stringResource(id = R.string.link_keywords),
        R.string.link_keyword_name to stringResource(id = R.string.link_keyword_name),
        R.string.link_keyword_original to stringResource(id = R.string.link_keyword_original),
        R.string.link_keyword_compare to stringResource(id = R.string.link_keyword_compare),
        R.string.link_keyword_intersection to stringResource(id = R.string.link_keyword_intersection),
        R.string.link_semantic_sim to stringResource(id = R.string.link_semantic_sim),
        R.string.link_relevant_sim to stringResource(id = R.string.link_relevant_sim),
    )

    val linkInfo = LinkInfo(
        url = "https://developer.android.com/develop/ui/compose/animation/composables-modifiers",
        semanticSimilarity = 0.75,
        originalText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor " +
                "in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                "proident, sunt in culpa qui officia deserunt mollit anim id est " +
                "laborum.",
        textComparison = listOf(
            DiffComparison(-1, "Lorem ipsum dolor sit amet"),
            DiffComparison(0, ", consectetur adipiscing elit,"),
            DiffComparison(1, "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ")
        ),
        keywordComparison = KeywordsComparing(
            original = listOf(Keyword("AS", listOf("sda", "dsadasda", "dsadasda"))),
            compare = listOf(Keyword("AS", listOf("sda", "dsadasda", "dsadasda"))),
            intersection = listOf(Keyword("AS", listOf("sda", "dsadasda", "dsadasda")))
        ),
        originalTitle = "title"
    )
    Surface(color = AppWhite, modifier = Modifier.fillMaxSize()) {
        LinkItemCustom(linkInfo = linkInfo, onUrlClick = {}, getString = { id -> stringMap[id]!! })
    }
}