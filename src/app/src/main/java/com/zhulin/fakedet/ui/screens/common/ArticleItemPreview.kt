package com.zhulin.fakedet.ui.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue

@Composable
fun ArticleItemPreview(
    text: String,
    onProcess: () -> Unit,
    onOpen: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val textExpanded = remember { mutableStateOf(false) }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .clickable {
                    textExpanded.value = !textExpanded.value
                }
                .fillMaxWidth(if (!textExpanded.value) 0.85f else 0.75f)
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.Left,
            maxLines = if (!textExpanded.value) 2 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onProcess) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gear_play),
                contentDescription = "",
                tint = AppBlue,
                modifier = Modifier.size(40.dp)
            )
        }

        AnimatedVisibility(visible = textExpanded.value) {
            IconButton(onClick = onOpen, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_open_link),
                    contentDescription = "",
                    tint = AppBlack,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ArticleItemPreviewPreview() {

    ArticleItemPreview(
        text = "Опубликовано видео с приближением к горизонту событий черной дыры",
        onProcess = {},
        onOpen = {}
    )
}