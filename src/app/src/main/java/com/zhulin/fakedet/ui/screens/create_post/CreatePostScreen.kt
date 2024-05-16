package com.zhulin.fakedet.ui.screens.create_post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
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
import com.zhulin.fakedet.business.models.ContentInfo
import com.zhulin.fakedet.business.models.ContentType
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.common.DialogImageMedia
import com.zhulin.fakedet.ui.screens.common.DialogMedia
import com.zhulin.fakedet.ui.screens.create_post.components.CustomTextField
import com.zhulin.fakedet.ui.screens.create_post.components.Keyboard
import com.zhulin.fakedet.ui.screens.create_post.components.keyboardAsState
import com.zhulin.fakedet.ui.screens.create_post.states.ScreenEvent
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppLightGreen
import com.zhulin.fakedet.ui.theme.AppWhite
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavHostController,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState = viewModel.screenState.value

    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel(ScreenEvent.AddUri(uri)) } }

    val keyboardStatus by keyboardAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CreatePostViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            event.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is CreatePostViewModel.UiEvent.BackToAuth -> navController.navigate(Screen.AuthorizationScreen.route)

                is CreatePostViewModel.UiEvent.BackToMenu -> navController.navigate(Screen.MainScreen.route)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppLightGreen,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopBarCustom(
                        onBack = { viewModel(ScreenEvent.BackToMenu) },
                        getString = { viewModel.getStringResource(it) }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppLightGreen),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp)
            )
        },
        bottomBar = {
            if (keyboardStatus == Keyboard.Closed)
                BottomBarCustom(
                    onAdd = { pickFileLauncher.launch("*/*") },
                    contents = screenState.contents,
                    onContentRemove = { viewModel(ScreenEvent.RemoveUri(it)) },
                    onSend = { viewModel(ScreenEvent.Send) },
                    getString = { viewModel.getStringResource(it) }
                )
        }
    ) { innerPadding ->

        TextInputScreen(
            innerPadding = innerPadding,
            text = screenState.text,
            isHintVisible = screenState.hintVisible,
            onValueChange = { viewModel(ScreenEvent.OnTextChange(it)) },
            onFocusChange = { viewModel(ScreenEvent.OnTextFocusChange(it)) },
            getString = { viewModel.getStringResource(it) },
        )
    }
}

@Composable
private fun TopBarCustom(
    onBack: () -> Unit, getString: (id: Int) -> String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Start),
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = AppBlack,
                modifier = Modifier.size(50.dp)
            )
        }

        Text(
            text = getString(R.string.create_create_request),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold, lineHeight = 24.sp, fontSize = 32.sp
            ),
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(0.6f)
        )

    }
}

@Preview(backgroundColor = 0xFFEAFFF8, showBackground = true)
@Composable
private fun TopBarCustomPreview() {
    val stringMap = mapOf(
        R.string.create_create_request to stringResource(id = R.string.create_create_request),
    )

    TopBarCustom(
        onBack = {},
        getString = { id -> stringMap[id]!! }
    )
}

@Composable
private fun TextInputScreen(
    innerPadding: PaddingValues,
    text: String,
    isHintVisible: Boolean,
    onValueChange: (it: String) -> Unit,
    onFocusChange: (it: FocusState) -> Unit,
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

    ) {
        CustomTextField(
            text = text,
            hint = getString(R.string.create_text_hint_placeholder),
            onValueChange = onValueChange,
            onFocusChange = onFocusChange,
            isHintVisible = isHintVisible,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 30.dp, vertical = 30.dp)
        )
        HorizontalDivider(thickness = 1.dp, color = AppBlack)
    }

}

@Preview(backgroundColor = 0xFFEAFFF8, showBackground = true)
@Composable
private fun TextInputScreenPreview() {
    val stringMap = mapOf(
        R.string.create_text_hint_placeholder to stringResource(id = R.string.create_text_hint_placeholder),
    )

    TextInputScreen(innerPadding = PaddingValues(),
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n" + "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?",
        isHintVisible = false,
        onValueChange = {},
        onFocusChange = {},
        getString = { id -> stringMap[id]!! })
}

@Composable
private fun BottomBarCustom(
    onAdd: () -> Unit,
    contents: List<ContentInfo>,
    onContentRemove: (uri: Uri) -> Unit,
    onSend: () -> Unit,
    getString: (id: Int) -> String
) {
    BottomAppBar(
        containerColor = AppWhite,
        actions = {
            IconButton(onClick = onAdd) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_attach),
                    contentDescription = "",
                    tint = AppBlue,
                    modifier = Modifier.size(50.dp)
                )
            }
            LazyRow {
                items(contents) {
                    val visibleMedia = remember { mutableStateOf(false) }

                    when (it.type) {
                        ContentType.PHOTO -> {
                            IconButton(onClick = { visibleMedia.value = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_image),
                                    contentDescription = "",
                                    tint = AppBlack,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(50.dp)
                                )
                            }

                            if (visibleMedia.value) {
                                DialogImageMedia(
                                    imageUri = it.uri,
                                    onDismissRequest = { visibleMedia.value = false },
                                    onContentRemove = { onContentRemove(it.uri) },
                                    getString = getString
                                )
                            }
                        }

                        ContentType.VIDEO -> {
                            IconButton(onClick = { visibleMedia.value = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_video),
                                    contentDescription = "",
                                    tint = AppBlack,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(50.dp)
                                )
                            }

                            if (visibleMedia.value) {
                                DialogMedia(
                                    mediaUri = it.uri,
                                    onDismissRequest = { visibleMedia.value = false },
                                    onContentRemove = { onContentRemove(it.uri) },
                                    getString = getString
                                )
                            }
                        }

                        ContentType.AUDIO -> {
                            IconButton(onClick = { visibleMedia.value = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_audio),
                                    contentDescription = "",
                                    tint = AppBlack,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(50.dp)
                                )
                            }

                            if (visibleMedia.value) {
                                DialogMedia(
                                    mediaUri = it.uri,
                                    onDismissRequest = { visibleMedia.value = false },
                                    onContentRemove = { onContentRemove(it.uri) },
                                    getString = getString
                                )
                            }
                        }

                        ContentType.NONE -> {}
                    }

                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSend,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "",
                    tint = AppBlack,
                    modifier = Modifier.size(50.dp)
                )
            }
        })
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun BottomBarCustomPreview() {
    BottomBarCustom(
        onAdd = {},
        contents = emptyList(),
        onContentRemove = {},
        onSend = {},
        getString = { "" }
    )
}

