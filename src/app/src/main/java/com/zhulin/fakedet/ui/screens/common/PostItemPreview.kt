package com.zhulin.fakedet.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.helpers.ColorHelpers.getTrustCircleColor
import com.zhulin.fakedet.ui.helpers.DateHelpers.customFormat
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppWhite
import java.time.LocalDateTime

@Composable
fun PostItemPreview(
    text: String,
    mediaCount: Int,
    trustValue: Int,
    checked: Boolean,
    dateTime: LocalDateTime,
    onClick: () -> Unit,
    getString: (id: Int) -> String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        if (checked) {
            val color = trustValue.getTrustCircleColor()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(50.dp)
            ) {
                CircularProgressIndicator(
                    progress = { maxOf(0.1f, (trustValue.toFloat() / 100)) },
                    modifier = Modifier.size(50.dp),
                    color = color,
                    strokeWidth = 3.dp,
                    trackColor = AppGray.copy(alpha = 0.5f),
                )
                Text(
                    text = "$trustValue %",
                    color = color,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 14.sp,
                        fontSize = 14.sp
                    )
                )
            }
        } else
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = AppWhite,
                trackColor = AppGray,
            )


        Text(
            text = text.ifBlank { "$mediaCount ${getString(R.string.main_files)}" },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.Left,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = dateTime.customFormat(getString),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Light,
                lineHeight = 12.sp,
                fontSize = 10.sp
            ),
            modifier = Modifier.fillMaxWidth(0.85f),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun PostItemPreviewPreview() {
    val stringMap = mapOf(
        R.string.main_files to stringResource(id = R.string.main_files),
        R.string.date_monday to stringResource(R.string.date_monday),
        R.string.date_tuesday to stringResource(R.string.date_tuesday),
        R.string.date_wednesday to stringResource(R.string.date_wednesday),
        R.string.date_thursday to stringResource(R.string.date_thursday),
        R.string.date_friday to stringResource(R.string.date_friday),
        R.string.date_saturday to stringResource(R.string.date_saturday),
        R.string.date_sunday to stringResource(R.string.date_sunday),
        R.string.date_today to stringResource(R.string.date_today),
        R.string.date_yesterday to stringResource(R.string.date_yesterday),
    )


    PostItemPreview(
        text = "",
        mediaCount = 0,
        trustValue = 100,
        checked = true,
        dateTime = LocalDateTime.of(2023, 10, 16, 22, 23),
        onClick = {},
        getString = { id -> stringMap[id]!! }
    )
}