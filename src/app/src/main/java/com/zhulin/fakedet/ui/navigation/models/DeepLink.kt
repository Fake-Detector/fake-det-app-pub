package com.zhulin.fakedet.ui.navigation.models

sealed class DeepLink(val link: String) {
    object PostScreen : DeepLink("https://fake-det.netlify.app/news")
}