package com.zhulin.fakedet.ui.screens.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.zhulin.fakedet.business.models.ItemPostType
import com.zhulin.fakedet.data.helpers.ItemHelpers.getAiTrust
import com.zhulin.fakedet.data.helpers.ItemHelpers.getLinks
import com.zhulin.fakedet.data.helpers.ItemHelpers.getMood
import com.zhulin.fakedet.data.helpers.ItemHelpers.getTags
import com.zhulin.fakedet.data.helpers.ItemHelpers.getTranscription
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.post.components.BottomBarCustom
import com.zhulin.fakedet.ui.screens.post.components.ImageItemScreen
import com.zhulin.fakedet.ui.screens.post.components.MediaItemScreen
import com.zhulin.fakedet.ui.screens.post.components.ProgressItemScreen
import com.zhulin.fakedet.ui.screens.post.components.TextItemScreen
import com.zhulin.fakedet.ui.screens.post.components.TopBarCustom
import com.zhulin.fakedet.ui.screens.post.states.ScreenEvent
import com.zhulin.fakedet.ui.theme.AppBlack
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavHostController,
    viewModel: PostViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState = viewModel.screenState.value

    if (!screenState.isInitialized) {
        LaunchedEffect(true) {
            viewModel(ScreenEvent.Initialize)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PostViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            event.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is PostViewModel.UiEvent.BackToMenu -> navController.navigate(event.route)
                is PostViewModel.UiEvent.BackToAuth -> navController.navigate(Screen.AuthorizationScreen.route)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (screenState.isInitialized) {
                        TopBarCustom(
                            title = screenState.post.title,
                            date = screenState.post.dateTime,
                            onBack = { viewModel(ScreenEvent.BackToMenu) },
                            onRefresh = { viewModel(ScreenEvent.Refresh) },
                            getString = { viewModel.getStringResource(it) }
                        )
                    } else {
                        TopBarCustom(
                            title = "",
                            date = LocalDateTime.now(),
                            onBack = { viewModel(ScreenEvent.BackToMenu) },
                            onRefresh = { viewModel(ScreenEvent.Refresh) },
                            getString = { viewModel.getStringResource(it) }
                        )
                    }

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppBlack),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp)
            )
        },
        bottomBar = {
            if (screenState.isInitialized) {
                viewModel.getItem()?.let { item ->
                    BottomBarCustom(
                        trustValue = item.trustValue,
                        isChecked = item.isChecked,
                        items = screenState.post.items,
                        onItemClick = { viewModel(ScreenEvent.SetItem(it)) })
                }

            }
        }
    ) { innerPadding ->
        if (screenState.isInitialized) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                viewModel.getItem()?.let { item ->
                    when (item.type) {
                        ItemPostType.TEXT -> TextItemScreen(
                            text = item.text,
                            trustValue = item.trustValue,
                            aiGenerated = item.getAiTrust(),
                            mood = item.getMood(),
                            tags = item.getTags(),
                            links = item.getLinks(),
                            isChecked = item.isChecked,
                            getString = { viewModel.getStringResource(it) }
                        )

                        ItemPostType.PHOTO -> ImageItemScreen(
                            imageUrl = item.url,
                            transcribe = item.getTranscription(),
                            trustValue = item.trustValue,
                            aiGenerated = item.getAiTrust(),
                            mood = item.getMood(),
                            tags = item.getTags(),
                            links = item.getLinks(),
                            isChecked = item.isChecked,
                            getString = { viewModel.getStringResource(it) }
                        )

                        ItemPostType.VIDEO -> MediaItemScreen(
                            mediaUrl = item.url,
                            transcribe = item.getTranscription(),
                            trustValue = item.trustValue,
                            aiGenerated = item.getAiTrust(),
                            mood = item.getMood(),
                            tags = item.getTags(),
                            links = item.getLinks(),
                            isChecked = item.isChecked,
                            getString = { viewModel.getStringResource(it) }
                        )

                        ItemPostType.AUDIO -> MediaItemScreen(
                            mediaUrl = item.url,
                            transcribe = item.getTranscription(),
                            trustValue = item.trustValue,
                            aiGenerated = item.getAiTrust(),
                            mood = item.getMood(),
                            tags = item.getTags(),
                            links = item.getLinks(),
                            isChecked = item.isChecked,
                            getString = { viewModel.getStringResource(it) }
                        )

                        ItemPostType.NONE -> ProgressItemScreen()
                    }
                } ?: run {
                    ProgressItemScreen()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                ProgressItemScreen()
            }
        }
    }
}




