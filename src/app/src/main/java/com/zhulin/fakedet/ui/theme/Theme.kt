package com.zhulin.fakedet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppBlack,
    secondary = AppGray,
    tertiary = AppWhite

)

@Composable
fun FakeDetTheme(content: @Composable () -> Unit) {
    val colors = LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}