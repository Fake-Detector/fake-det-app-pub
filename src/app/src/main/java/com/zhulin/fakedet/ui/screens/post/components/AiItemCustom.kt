package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getTrustCircleColor
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlackGreen
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppRed
import com.zhulin.fakedet.ui.theme.AppWhite

@Composable
fun AiItemCustom(
    sentence: String,
    humanMade: Double,
    getString: (id: Int) -> String
) {
    val humanMadePercentage = humanMade.toInt()
    val color = humanMadePercentage.getTrustCircleColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .border(2.dp, color, RoundedCornerShape(25.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val text = remember { mutableStateOf(sentence) }
        val textExpanded = remember { mutableStateOf(true) }

        Text(
            text = text.value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                lineHeight = 14.sp,
                fontSize = 14.sp,
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
            maxLines = if (textExpanded.value) 4 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(10.dp))

        AiGeneratedRow(humanMade, getString)
    }
}

@Composable
fun AiGeneratedRow(
    humanMade: Double,
    getString: (id: Int) -> String
) {
    val humanMadePercentage = humanMade.toInt()
    val color = humanMadePercentage.getTrustCircleColor()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getString(R.string.ai_generated_text),
            color = AppRed,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 13.sp,
                fontSize = 13.sp
            ),
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { maxOf(0.1f, (humanMade / 100).toFloat()) },
                color = color,
                trackColor = AppGray.copy(alpha = 0.5f),
            )

            Text(
                text = "$humanMadePercentage %",
                color = color,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = getString(R.string.humand_made_text),
            color = AppBlackGreen,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 13.sp,
                fontSize = 13.sp
            ),
            textAlign = TextAlign.Center
        )

    }
}

@Preview
@Composable
private fun AiItemCustomPreview() {
    val stringMap = mapOf(
        R.string.humand_made_text to stringResource(id = R.string.humand_made_text),
        R.string.ai_generated_text to stringResource(id = R.string.ai_generated_text),
    )

    AiItemCustom(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor " +
                "in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                "proident, sunt in culpa qui officia deserunt mollit anim id est " +
                "laborum.",
        90.0,
        getString = { id -> stringMap[id]!! }
    )
}