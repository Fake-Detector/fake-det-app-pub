package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.helpers.DateHelpers.customFormat
import com.zhulin.fakedet.ui.theme.AppWhite
import java.time.LocalDateTime

@Composable
fun TopBarCustom(
    title: String,
    date: LocalDateTime,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    getString: (id: Int) -> String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = AppWhite,
                modifier = Modifier.size(50.dp)
            )
        }

        Text(
            text = if (title.isNotBlank()) {
                buildAnnotatedString { append(title) }
            } else {
                buildAnnotatedString {
                    append(getString(R.string.post_post_title))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(" ")
                        append(date.customFormat(getString))
                    }
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold, lineHeight = 28.sp, fontSize = 32.sp, color = AppWhite
            ),
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 10.dp)
        )

        IconButton(onClick = onRefresh) {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = "",
                tint = AppWhite,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Preview(backgroundColor = 0xFF01110A, showBackground = true)
@Composable
private fun TopBarCustomPreview() {
    val stringMap = mapOf(
        R.string.date_monday to stringResource(R.string.date_monday),
        R.string.date_tuesday to stringResource(R.string.date_tuesday),
        R.string.date_wednesday to stringResource(R.string.date_wednesday),
        R.string.date_thursday to stringResource(R.string.date_thursday),
        R.string.date_friday to stringResource(R.string.date_friday),
        R.string.date_saturday to stringResource(R.string.date_saturday),
        R.string.date_sunday to stringResource(R.string.date_sunday),
        R.string.date_today to stringResource(R.string.date_today),
        R.string.date_yesterday to stringResource(R.string.date_yesterday),
        R.string.post_post_title to stringResource(id = R.string.post_post_title),
    )

    TopBarCustom(
        title = "",
        date = LocalDateTime.now(),
        onBack = {},
        onRefresh = {},
        getString = { id -> stringMap[id]!! }
    )
}