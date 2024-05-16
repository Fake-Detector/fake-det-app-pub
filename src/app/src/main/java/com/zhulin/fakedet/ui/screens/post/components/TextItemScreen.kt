package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.models.AiInfos
import com.zhulin.fakedet.business.models.LinkInfos
import com.zhulin.fakedet.business.models.SententeceAiInfo
import com.zhulin.fakedet.ui.theme.AppBlack

@Composable
fun TextItemScreen(
    text: String,
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
            val textExpanded = remember { mutableStateOf(true) }
            Text(
                text = text,
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
        },
        trustValue = trustValue,
        aiGenerated = aiGenerated,
        tags = tags,
        mood = mood,
        links = links,
        isChecked = isChecked,
        getString = getString
    )
}

@Preview
@Composable
private fun TextItemScreenPreview() {
    val stringMap = mapOf(
        R.string.create_create_request to stringResource(id = R.string.create_create_request),
        R.string.post_confidence_level to stringResource(id = R.string.post_confidence_level),
        R.string.post_result to stringResource(id = R.string.post_result),
        R.string.post_ai_generated to stringResource(id = R.string.post_ai_generated),
        R.string.post_topic to stringResource(id = R.string.post_topic),
        R.string.post_mood to stringResource(id = R.string.post_mood),
        R.string.post_confidence_level to stringResource(id = R.string.post_confidence_level),
        R.string.post_links to stringResource(id = R.string.post_links),
        R.string.post_trusted to stringResource(id = R.string.post_trusted),
        R.string.post_untrusted to stringResource(id = R.string.post_untrusted),
        R.string.humand_made_text to stringResource(id = R.string.humand_made_text),
        R.string.ai_generated_text to stringResource(id = R.string.ai_generated_text),
    )

    TextItemScreen(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor " +
                "in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                "proident, sunt in culpa qui officia deserunt mollit anim id est " +
                "laborum.",
        trustValue = 80,
        aiGenerated = AiInfos(
            overallHumanMade = 0.6,
            sentences = listOf(
                SententeceAiInfo(humanMade = 0.9, sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "),
                SententeceAiInfo(humanMade = 0.5, sentence = "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "),
                SententeceAiInfo(humanMade = 0.1, sentence = "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor "),
            )
        ),
        mood = "Positive",
        tags = listOf(
            "AI",
            "IT",
            "dsadasdasdhjas"
        ),
        links = LinkInfos(),
        isChecked = true,
        getString = { id -> stringMap[id]!! }
    )
}