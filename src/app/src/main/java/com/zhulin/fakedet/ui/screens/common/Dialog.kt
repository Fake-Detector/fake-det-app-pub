package com.zhulin.fakedet.ui.screens.common

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppGray
import com.zhulin.fakedet.ui.theme.AppRed
import com.zhulin.fakedet.ui.theme.AppWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DIALOG_BUILD_TIME = 150L
const val ANIMATION_TIME = 500L

private suspend fun startDismissWithExitAnimation(
    animateTrigger: MutableState<Boolean>,
    onDismissRequest: () -> Unit
) {
    animateTrigger.value = false
    delay(ANIMATION_TIME)
    onDismissRequest()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
    content: @Composable () -> Unit,
    title: String?,
    onDismissRequest: () -> Unit,
) {
    val animateTrigger = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animateTrigger.value = true
        }
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest
    ) {
        AnimatedVisibility(
            visible = animateTrigger.value,
            enter = fadeIn(animationSpec = tween(ANIMATION_TIME.toInt())),
            exit = fadeOut(animationSpec = tween(ANIMATION_TIME.toInt()))
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = AppBlack,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    alignment = Alignment.Start
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = onDismissRequest) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "",
                                        tint = AppWhite,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }

                                Text(
                                    text = buildAnnotatedString { append(title ?: "") },
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 22.sp,
                                        fontSize = 24.sp,
                                        color = AppWhite
                                    ),
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .padding(vertical = 10.dp),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        },
                        modifier = Modifier.padding(vertical = 30.dp),
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Transparent)
                    )
                }
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(topStartPercent = 15, topEndPercent = 15))
                        .background(AppWhite)
                        .padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 10.dp)
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            content()
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun DialogMedia(
    content: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    onContentRemove: () -> Unit,
    getString: (id: Int) -> String
) {
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()
    val animateTrigger = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animateTrigger.value = true
        }
    }

    Dialog(onDismissRequest = {
        coroutineScope.launch {
            startDismissWithExitAnimation(animateTrigger, onDismissRequest)
        }
    }) {
        AnimatedVisibility(
            visible = animateTrigger.value,
            enter = fadeIn(animationSpec = tween(ANIMATION_TIME.toInt())),
            exit = fadeOut(animationSpec = tween(ANIMATION_TIME.toInt()))
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        coroutineScope.launch {
                            startDismissWithExitAnimation(animateTrigger, onDismissRequest)
                        }
                    }
            ) {
                content()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                startDismissWithExitAnimation(animateTrigger, onDismissRequest)
                            }
                        },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppGray)
                    ) {
                        Text(
                            text = getString(R.string.create_close_dialog),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 18.sp,
                                fontSize = 18.sp,
                                color = AppWhite
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Surface(modifier = Modifier.weight(0.1f)) {
                    }

                    Button(
                        onClick = { onContentRemove() },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppRed)
                    ) {
                        Text(
                            text = getString(R.string.create_remove_dialog),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 18.sp,
                                fontSize = 18.sp,
                                color = AppWhite
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialogImageMedia(
    imageUri: Uri,
    onDismissRequest: () -> Unit,
    onContentRemove: () -> Unit,
    getString: (id: Int) -> String
) {
    var scale by remember { mutableFloatStateOf(1f) }

    DialogMedia(
        content = {
            Box(
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            scale *= zoom
                        }
                    }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.75f)
                        .graphicsLayer(
                            scaleX = maxOf(1f, scale),
                            scaleY = maxOf(1f, scale),
                            translationX = 0f,
                            translationY = 0f
                        ),
                )
            }
        },
        onDismissRequest = onDismissRequest,
        onContentRemove = onContentRemove,
        getString = getString
    )
}

@Composable
fun DialogMedia(
    mediaUri: Uri,
    onDismissRequest: () -> Unit,
    onContentRemove: () -> Unit,
    getString: (id: Int) -> String
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(mediaUri))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    DialogMedia(
        content = {
            Box(
                modifier = Modifier
            ) {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize(0.75f),
                )
            }
        },
        onDismissRequest = onDismissRequest,
        onContentRemove = onContentRemove,
        getString = getString
    )
}