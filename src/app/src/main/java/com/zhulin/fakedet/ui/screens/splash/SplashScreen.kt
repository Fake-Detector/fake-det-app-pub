package com.zhulin.fakedet.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppRed
import com.zhulin.fakedet.ui.theme.AppWhite
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@ExperimentalMaterial3Api
@Composable
fun SplashScreen(
    navController: NavHostController,
) {
    LaunchedEffect(key1 = true) {
        delay(1.seconds)

        navController.navigate(Screen.AuthorizationScreen.route)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AppWhite)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = AppRed)) {
                        append("Fake")
                    }
                    withStyle(style = SpanStyle(color = AppBlack)) {
                        append("DET")
                    }
                }, style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 48.sp
                )
            )
        }
    }
}
