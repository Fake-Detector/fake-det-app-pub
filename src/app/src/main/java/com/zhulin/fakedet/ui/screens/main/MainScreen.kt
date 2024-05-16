package com.zhulin.fakedet.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.models.PostInfo
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.common.BottomApplicationBar
import com.zhulin.fakedet.ui.screens.common.PostItemPreview
import com.zhulin.fakedet.ui.screens.main.states.ScreenEvent
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppLightGreen
import com.zhulin.fakedet.ui.theme.AppWhite
import fake.detection.post.bridge.contracts.PostOuterClass.DataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState = viewModel.screenState.value

    val refreshState = rememberPullToRefreshState()

    if (!screenState.isInitialized) {
        LaunchedEffect(true) {
            viewModel(ScreenEvent.Initialize)
        }
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel(ScreenEvent.GetPostHistory)
            refreshState.endRefresh()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MainViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            event.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is MainViewModel.UiEvent.BackToAuth -> navController.navigate(Screen.AuthorizationScreen.route)

                is MainViewModel.UiEvent.SignOut -> navController.navigate(Screen.SplashScreen.route)

                is MainViewModel.UiEvent.CreatePost -> navController.navigate(Screen.CreatePostScreen.route)

                is MainViewModel.UiEvent.ShowNews -> navController.navigate(Screen.NewsScreen.route)

                is MainViewModel.UiEvent.GetPost -> navController.navigate(Screen.PostScreen.route + "?postId=${event.postId}&dataSource=${DataSource.Author}")

                is MainViewModel.UiEvent.ShowTelegram -> navController.navigate(Screen.TelegramScreen.route)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppLightGreen,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopBarCustom(
                        userName = screenState.userName,
                        onSettingsClick = { viewModel(ScreenEvent.ChangeSettingsExpended) },
                        settingsExpended = screenState.settingsExpended,
                        onSettingsDismiss = { viewModel(ScreenEvent.ChangeSettingsExpended) },
                        onLanguageChange = { viewModel(ScreenEvent.ChangeLanguage) },
                        onSignOut = { viewModel(ScreenEvent.SignOut) },
                        getString = { viewModel.getStringResource(it) })
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppLightGreen),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp)
            )
        },
        bottomBar = {
            BottomApplicationBar(
                onBotClick = { },
                botSelected = true,
                onTGClick = { viewModel(ScreenEvent.ShowTelegram) },
                tgSelected = false,
                onNewsClick = { viewModel(ScreenEvent.ShowNews) },
                newsSelected = false,
            )
        }
    ) { innerPadding ->
        HistoryScreen(
            innerPadding = innerPadding,
            onCreatePost = { viewModel(ScreenEvent.CreatePost) },
            posts = screenState.userPosts.posts,
            onPostClick = { viewModel(ScreenEvent.GetPost(it)) },
            postRefresh = refreshState.isRefreshing,
            scrollConnection = refreshState.nestedScrollConnection,
            refreshProgress = refreshState.progress,
            isInitialized = screenState.isInitialized,
            getString = { viewModel.getStringResource(it) })
    }
}

@Composable
private fun HistoryScreen(
    innerPadding: PaddingValues,
    onCreatePost: () -> Unit,
    posts: List<PostInfo>,
    onPostClick: (id: Long) -> Unit,
    postRefresh: Boolean,
    scrollConnection: NestedScrollConnection,
    refreshProgress: Float,
    isInitialized: Boolean,
    getString: (id: Int) -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(topStartPercent = 15, topEndPercent = 15))
            .background(AppWhite)
            .padding(horizontal = 30.dp)
            .padding(top = 30.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = getString(R.string.main_history),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 40.sp,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = onCreatePost) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "",
                    tint = AppBlue,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(isInitialized) {
                AnimatedVisibility(postRefresh) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(1f),
                        color = AppGray
                    )
                }
                AnimatedVisibility(visible = !postRefresh) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(1f),
                        progress = { refreshProgress },
                        color = AppGray,
                        trackColor = Color.Transparent
                    )
                }
                AnimatedVisibility(
                    visible = !postRefresh
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                        contentPadding = PaddingValues(top = 10.dp),
                        modifier = Modifier
                            .nestedScroll(scrollConnection)
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        items(posts) { postInfo ->
                            PostItemPreview(
                                text = postInfo.text,
                                mediaCount = postInfo.mediaCount,
                                trustValue = postInfo.trustValue,
                                checked = postInfo.checked,
                                dateTime = postInfo.dateTime,
                                onClick = { onPostClick(postInfo.id) },
                                getString = getString
                            )
                        }

                        if (posts.isEmpty()) {
                            item {
                                Text(
                                    text = getString(R.string.post_empty),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        lineHeight = 16.sp,
                                        fontSize = 16.sp
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .padding(horizontal = 10.dp),
                                    textAlign = TextAlign.Left,
                                )
                            }
                        }
                    }
                }

            }

            AnimatedVisibility(!isInitialized) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = AppWhite,
                    trackColor = AppGray,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HistoryScreenPreview() {
    val stringMap = mapOf(
        R.string.main_history to stringResource(id = R.string.main_history),
        R.string.date_monday to stringResource(R.string.date_monday),
        R.string.date_tuesday to stringResource(R.string.date_tuesday),
        R.string.date_wednesday to stringResource(R.string.date_wednesday),
        R.string.date_thursday to stringResource(R.string.date_thursday),
        R.string.date_friday to stringResource(R.string.date_friday),
        R.string.date_saturday to stringResource(R.string.date_saturday),
        R.string.date_sunday to stringResource(R.string.date_sunday),
        R.string.date_today to stringResource(R.string.date_today),
        R.string.date_yesterday to stringResource(R.string.date_yesterday),
        R.string.post_empty to stringResource(R.string.post_empty),
    )

    val posts = listOf(
        PostInfo(0, "0", "SOME TEXT", 3, 55, true, LocalDateTime.now()),
        PostInfo(1, "0", "SOME TEXT2", 3, 99, true, LocalDateTime.now()),
        PostInfo(1, "0", "SOME TEXT3", 3, 99, false, LocalDateTime.now())
    )

    val refreshState = rememberPullToRefreshState()


    HistoryScreen(
        innerPadding = PaddingValues(),
        onCreatePost = {},
        posts = posts,
        onPostClick = {},
        postRefresh = refreshState.isRefreshing,
        scrollConnection = refreshState.nestedScrollConnection,
        refreshProgress = 1f,
        isInitialized = true,
        getString = { id -> stringMap[id]!! }
    )
}


@Composable
private fun TopBarCustom(
    userName: String,
    settingsExpended: Boolean,
    onSettingsClick: () -> Unit,
    onSettingsDismiss: () -> Unit,
    onLanguageChange: () -> Unit,
    onSignOut: () -> Unit,
    getString: (id: Int) -> String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "",
            tint = AppBlue,
            modifier = Modifier.size(50.dp)
        )

        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = getString(R.string.main_welcome),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    lineHeight = 18.sp,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "",
                    tint = AppBlack,
                    modifier = Modifier.size(30.dp)
                )
            }
            DropdownMenu(
                expanded = settingsExpended,
                onDismissRequest = onSettingsDismiss,
                modifier = Modifier.background(color = AppWhite)
            ) {
                DropdownMenuItem(
                    onClick = onLanguageChange,
                    text = {
                        Text(
                            text = getString(R.string.main_settings_language),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Light,
                                lineHeight = 18.sp,
                                fontSize = 16.sp
                            ),
                            textAlign = TextAlign.Left
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_language),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = AppBlack
                        )
                    }
                )
                DropdownMenuItem(
                    onClick = onSignOut,
                    text = {
                        Text(
                            text = getString(R.string.main_settings_sign_out),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Light,
                                lineHeight = 18.sp,
                                fontSize = 16.sp
                            ),
                            textAlign = TextAlign.Left
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = AppBlack
                        )
                    }
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFEAFFF8, showBackground = true)
@Composable
private fun TopBarCustomPreview() {
    val stringMap = mapOf(
        R.string.main_welcome to stringResource(id = R.string.main_welcome),
        R.string.main_settings_language to stringResource(id = R.string.main_settings_language),
        R.string.main_settings_sign_out to stringResource(id = R.string.main_settings_sign_out),
    )

    TopBarCustom(
        userName = "Artem",
        settingsExpended = false,
        onSettingsClick = {},
        onSettingsDismiss = {},
        onLanguageChange = {},
        onSignOut = {},
        getString = { id -> stringMap[id]!! })
}