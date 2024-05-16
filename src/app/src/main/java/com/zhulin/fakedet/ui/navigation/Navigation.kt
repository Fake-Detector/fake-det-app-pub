package com.zhulin.fakedet.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.zhulin.fakedet.ui.navigation.models.DeepLink
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.authorization.AuthorizationScreen
import com.zhulin.fakedet.ui.screens.create_post.CreatePostScreen
import com.zhulin.fakedet.ui.screens.main.MainScreen
import com.zhulin.fakedet.ui.screens.news.NewsScreen
import com.zhulin.fakedet.ui.screens.post.PostScreen
import com.zhulin.fakedet.ui.screens.splash.SplashScreen
import com.zhulin.fakedet.ui.screens.telegram.TelegramScreen

@ExperimentalMaterial3Api
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.SplashScreen.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.SplashScreen.route,
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() })
        {
            SplashScreen(navController = navController)
        }
        composable(Screen.AuthorizationScreen.route,
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() }) {
            AuthorizationScreen(navController = navController)
        }
        composable(Screen.MainScreen.route,
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() }) {
            MainScreen(navController = navController)
        }

        composable(Screen.CreatePostScreen.route,
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() }) {
            CreatePostScreen(navController = navController)
        }

        composable(
            route = Screen.NewsScreen.route,
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() }) {

            NewsScreen(navController = navController)
        }

        composable(
            route = Screen.TelegramScreen.route,
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() }) {

            TelegramScreen(navController = navController)
        }

        composable(
            route = Screen.PostScreen.route + "?postId={postId}&dataSource={dataSource}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DeepLink.PostScreen.link}/{postId}/{dataSource}"
                }
            ),
            arguments = listOf(
                navArgument(
                    name = "postId"
                ) {
                    type = NavType.LongType
                    defaultValue = 0
                },
                navArgument(
                    name = "dataSource"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer() }
        ) {
            PostScreen(navController = navController)
        }
    }
}

fun scaleIntoContainer(
    initialScale: Float = 1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    targetScale: Float = 1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}