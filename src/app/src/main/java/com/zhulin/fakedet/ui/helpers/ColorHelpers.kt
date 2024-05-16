package com.zhulin.fakedet.ui.helpers

import androidx.compose.ui.graphics.Color
import com.zhulin.fakedet.ui.theme.AppBlackGreen
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppGreenBackground
import com.zhulin.fakedet.ui.theme.AppPinkBackground
import com.zhulin.fakedet.ui.theme.AppRed
import com.zhulin.fakedet.ui.theme.AppYellow
import com.zhulin.fakedet.ui.theme.AppYellowBackground

object ColorHelpers {
    fun Int.getTrustCircleColor(): Color = when (this) {
        in 0..39 -> AppRed
        in 40..79 -> AppYellow
        in 80..100 -> AppBlackGreen
        else -> AppGray
    }

    fun Int.getCompareCircleColor(): Color = when (this) {
        in 0..50 -> AppRed
        in 50..79 -> AppYellow
        in 80..100 -> AppBlackGreen
        else -> AppGray
    }

    fun Int.getTrustBackgroundColor(): Color = when (this) {
        in 0..39 -> AppPinkBackground
        in 40..79 -> AppYellowBackground
        in 80..100 -> AppGreenBackground
        else -> AppGray
    }
}