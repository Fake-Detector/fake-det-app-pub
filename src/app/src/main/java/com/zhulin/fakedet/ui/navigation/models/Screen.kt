package com.zhulin.fakedet.ui.navigation.models

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash")
    object AuthorizationScreen : Screen("authorization")
    object MainScreen : Screen("main")
    object CreatePostScreen : Screen("create_post")
    object PostScreen : Screen("post")
    object NewsScreen : Screen("news")
    object TelegramScreen : Screen("telegram")
}
