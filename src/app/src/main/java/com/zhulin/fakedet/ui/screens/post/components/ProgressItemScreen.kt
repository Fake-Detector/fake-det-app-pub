package com.zhulin.fakedet.ui.screens.post.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppWhite

@Composable
fun ProgressItemScreen() {
    ItemScreen(
        content = {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = AppWhite,
                trackColor = AppGray,
            )
        }
    )
}

@Preview
@Composable
fun ProgressItemScreenPreview() {
    ProgressItemScreen()
}

