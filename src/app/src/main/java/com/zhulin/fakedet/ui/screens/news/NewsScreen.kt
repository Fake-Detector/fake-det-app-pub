package com.zhulin.fakedet.ui.screens.news

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
import com.zhulin.fakedet.business.models.ShortNewsInfo
import com.zhulin.fakedet.ui.helpers.SiteHelpers
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.common.ArticleItemPreview
import com.zhulin.fakedet.ui.screens.common.BottomApplicationBar
import com.zhulin.fakedet.ui.screens.common.PostItemPreview
import com.zhulin.fakedet.ui.screens.create_post.components.Keyboard
import com.zhulin.fakedet.ui.screens.create_post.components.keyboardAsState
import com.zhulin.fakedet.ui.screens.news.states.ScreenEvent
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppLightGreen
import com.zhulin.fakedet.ui.theme.AppWhite
import fake.detection.post.bridge.api.Bridge.Site
import fake.detection.post.bridge.contracts.PostOuterClass
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    navController: NavHostController,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState = viewModel.screenState.value
    val context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val keyboardStatus by keyboardAsState()

    if (!screenState.isInitialized) {
        LaunchedEffect(true) {
            viewModel(ScreenEvent.Initialize)
        }
    }

    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel(ScreenEvent.Refresh)
            refreshState.endRefresh()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NewsViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            event.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is NewsViewModel.UiEvent.BackToAuth -> navController.navigate(Screen.AuthorizationScreen.route)

                is NewsViewModel.UiEvent.ShowBot -> navController.navigate(Screen.MainScreen.route)

                is NewsViewModel.UiEvent.GetPost -> navController.navigate(Screen.PostScreen.route + "?postId=${event.postId}&dataSource=${PostOuterClass.DataSource.News}")

                is NewsViewModel.UiEvent.ShowTelegram -> navController.navigate(Screen.TelegramScreen.route)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppLightGreen,
        topBar = {
            TopBarCustom(
                selectedSite = screenState.site,
                onSelectSite = { viewModel(ScreenEvent.ChangeSite(it)) },
                onProcess = { viewModel(ScreenEvent.CheckNews(it)) },
                getString = { viewModel.getStringResource(it) },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 25.dp)
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            AnimatedVisibility(keyboardStatus == Keyboard.Closed) {
                BottomApplicationBar(
                    onBotClick = { viewModel(ScreenEvent.ShowBot) },
                    botSelected = false,
                    onTGClick = { viewModel(ScreenEvent.ShowTelegram) },
                    tgSelected = false,
                    onNewsClick = { },
                    newsSelected = true,
                )
            }
        }
    ) { innerPadding ->
        HistoryScreen(
            innerPadding = innerPadding,
            posts = screenState.newsPosts.posts,
            articles = screenState.newsArticles.articles,
            onPostClick = { viewModel(ScreenEvent.GetPost(it)) },
            onCheckArticle = { viewModel(ScreenEvent.CheckNews(it)) },
            onLinkOpen = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it)
                )
                context.startActivity(intent)
            },
            postRefresh = refreshState.isRefreshing,
            scrollConnection = refreshState.nestedScrollConnection,
            refreshProgress = refreshState.progress,
            onPrev = { viewModel(ScreenEvent.GetPrevArticles) },
            prevVisible = screenState.isPrevPagePossible,
            onNext = { viewModel(ScreenEvent.GetNextArticles) },
            nextVisible = screenState.isNextPagePossible,
            isInitialized = screenState.isInitialized,
            getString = { viewModel.getStringResource(it) })
    }
}

@Composable
private fun HistoryScreen(
    innerPadding: PaddingValues,
    posts: List<PostInfo>,
    articles: List<ShortNewsInfo>,
    onPostClick: (id: Long) -> Unit,
    onCheckArticle: (id: String) -> Unit,
    onLinkOpen: (url: String) -> Unit,
    postRefresh: Boolean,
    scrollConnection: NestedScrollConnection,
    refreshProgress: Float,
    onPrev: () -> Unit,
    prevVisible: Boolean,
    onNext: () -> Unit,
    nextVisible: Boolean,
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
                visible = !postRefresh && articles.isNotEmpty()
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

                    items(articles) { articleInfo ->
                        val postInfo =
                            posts.firstOrNull { it.externalId.equals(articleInfo.url, true) }
                        if (postInfo != null) {
                            PostItemPreview(
                                text = postInfo.text,
                                mediaCount = postInfo.mediaCount,
                                trustValue = postInfo.trustValue,
                                checked = postInfo.checked,
                                dateTime = postInfo.dateTime,
                                onClick = { onPostClick(postInfo.id) },
                                getString = getString
                            )
                        } else {
                            ArticleItemPreview(
                                text = articleInfo.content,
                                onProcess = { onCheckArticle(articleInfo.url) },
                                onOpen = { onLinkOpen(articleInfo.url) }
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (prevVisible) {
                                Button(
                                    onClick = onPrev,
                                    colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = getString(R.string.btn_prev),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 14.sp,
                                            fontSize = 14.sp,
                                            color = AppWhite
                                        ),
                                        modifier = Modifier,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(if (prevVisible && nextVisible) 1f else 2f))

                            if (nextVisible) {
                                Button(
                                    onClick = onNext,
                                    colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = getString(R.string.btn_next),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 14.sp,
                                            fontSize = 14.sp,
                                            color = AppWhite
                                        ),
                                        modifier = Modifier,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !postRefresh && articles.isEmpty()
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
                }
            }

            AnimatedVisibility(posts.isEmpty() && articles.isEmpty()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    contentPadding = PaddingValues(top = 10.dp),
                    modifier = Modifier
                        .nestedScroll(scrollConnection)
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    item {
                        Text(
                            text = getString(R.string.news_empty),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 24.sp,
                                fontSize = 24.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(horizontal = 10.dp),
                            textAlign = TextAlign.Center,
                        )
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
        R.string.btn_prev to stringResource(R.string.btn_prev),
        R.string.btn_next to stringResource(R.string.btn_next),
        R.string.news_empty to stringResource(R.string.news_empty),
    )

    val posts = listOf(
        PostInfo(0, "0", "SOME TEXT", 3, 55, true, LocalDateTime.now()),
        PostInfo(1, "1", "SOME TEXT2", 3, 99, true, LocalDateTime.now()),
        PostInfo(1, "2", "SOME TEXT3", 3, 99, false, LocalDateTime.now())
    )

    val articles = listOf(
        ShortNewsInfo("0", "Опубликовано видео с приближением к горизонту событий черной дыры"),
        ShortNewsInfo("2", "Опубликовано видео с приближением к горизонту событий черной дыры"),
        ShortNewsInfo("3", "Опубликовано видео с приближением к горизонту событий черной дыры"),
        ShortNewsInfo("1", "Опубликовано видео с приближением к горизонту событий черной дыры"),
        ShortNewsInfo("4", "Опубликовано видео с приближением к горизонту событий черной дыры"),
    )

    val refreshState = rememberPullToRefreshState()


    HistoryScreen(
        innerPadding = PaddingValues(),
        posts = posts,
        articles = articles,
        onPostClick = {},
        onCheckArticle = {},
        onLinkOpen = {},
        postRefresh = refreshState.isRefreshing,
        scrollConnection = refreshState.nestedScrollConnection,
        refreshProgress = 1f,
        onPrev = {},
        prevVisible = true,
        onNext = {},
        nextVisible = true,
        isInitialized = true,
        getString = { id -> stringMap[id]!! }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarCustom(
    selectedSite: Site,
    onSelectSite: (site: Site) -> Unit,
    onProcess: (url: String) -> Unit,
    getString: (id: Int) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            val selectedSiteInfo = SiteHelpers.getSites().firstOrNull { it.type == selectedSite }
            if (selectedSiteInfo != null) {
                Row(
                    modifier = Modifier
                        .menuAnchor()
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .border(1.dp, AppBlack, RoundedCornerShape(50.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = selectedSiteInfo.logoId),
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(1.dp, AppBlack, CircleShape)
                            .background(AppWhite),

                        contentDescription = ""
                    )
                    Text(
                        text = getString(selectedSiteInfo.nameId),
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
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }

            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(surface = Color.Transparent),
                shapes = MaterialTheme.shapes.copy(RoundedCornerShape(50.dp)),
            ) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                    SiteHelpers.getSites().forEach { siteInfo ->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onSelectSite(siteInfo.type)
                            },
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(Color.Transparent),
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        painter = painterResource(id = siteInfo.logoId),
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, AppBlack, CircleShape)
                                            .background(AppWhite),

                                        contentDescription = ""
                                    )
                                    Text(
                                        text = getString(siteInfo.nameId),
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
                        )
                    }

                }
            }
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .padding(horizontal = 25.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = remember { mutableStateOf("") }
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                placeholder = { Text(text = getString(R.string.enter_url)) },
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppWhite,
                    unfocusedContainerColor = AppWhite
                ),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { onProcess(text.value) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_gear_play),
                            contentDescription = "",
                            tint = AppBlue,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )

        }

    }
}

@Preview(backgroundColor = 0xFFEAFFF8, showBackground = true)
@Composable
private fun TopBarCustomPreview() {
    val stringMap = mapOf(
        R.string.site_iz to stringResource(id = R.string.site_iz),
        R.string.site_bbc to stringResource(id = R.string.site_bbc),
        R.string.site_cnn to stringResource(id = R.string.site_cnn),
        R.string.site_interfax to stringResource(id = R.string.site_interfax),
        R.string.site_lentaru to stringResource(id = R.string.site_lentaru),
        R.string.site_ria to stringResource(id = R.string.site_ria),
        R.string.site_tass to stringResource(id = R.string.site_tass),
        R.string.enter_url to stringResource(id = R.string.enter_url),
    )

    TopBarCustom(
        selectedSite = Site.LentaRu,
        onSelectSite = {},
        onProcess = {},
        getString = { id -> stringMap[id]!! })
}