package com.zhulin.fakedet.ui.screens.telegram

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.common.BottomApplicationBar
import com.zhulin.fakedet.ui.screens.telegram.states.ScreenEvent
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppWhite
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun TelegramScreen(
    navController: NavHostController,
    viewModel: TelegramViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState = viewModel.screenState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TelegramViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            event.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is TelegramViewModel.UiEvent.BackToAuth -> navController.navigate(Screen.AuthorizationScreen.route)
                is TelegramViewModel.UiEvent.ShowNews -> navController.navigate(Screen.NewsScreen.route)
                is TelegramViewModel.UiEvent.ShowBot -> navController.navigate(Screen.MainScreen.route)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppBlack,
        topBar = {
        },
        bottomBar = {
            BottomApplicationBar(
                onBotClick = { viewModel(ScreenEvent.ShowBot) },
                botSelected = false,
                onTGClick = { },
                tgSelected = true,
                onNewsClick = { viewModel(ScreenEvent.ShowNews) },
                newsSelected = false,
            )
        }
    ) { innerPadding ->
        TelegramLink(
            innerPadding = innerPadding,
            token = screenState.token,
            onLink = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(viewModel.getStringResource(R.string.bot_url))
                )
                context.startActivity(intent)
            },
            onCopy = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("token", it)
                clipboard.setPrimaryClip(clip)

            },
            getString = { viewModel.getStringResource(it) },
        )
    }
}

@Composable
private fun TelegramLink(
    innerPadding: PaddingValues,
    token: String,
    onLink: () -> Unit,
    onCopy: (token: String) -> Unit,
    getString: (id: Int) -> String,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(AppWhite)
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_transparent),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentDescription = "logo"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ClickableText(
            text = buildAnnotatedString {
                append("${getString(R.string.link_tg)} ")

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = AppBlue
                    )
                )
                {
                    append(getString(R.string.bot_name))
                }

            },
            onClick = { onLink() },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                fontSize = 16.sp,
                color = AppBlack,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(15.dp))

        Row {
            OutlinedTextField(
                value = TextFieldValue(annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = AppBlue, fontWeight = FontWeight.Bold)
                    ) {
                        append("/link ")
                    }

                    withStyle(
                        style = SpanStyle(color = AppBlack)
                    ) {
                        append(token)
                    }
                }),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppWhite,
                    unfocusedContainerColor = AppWhite
                ),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = { onCopy("/link $token") }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_copy_clipboard),
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

@Preview
@Composable
private fun TelegramLinkPreview(
) {
    val stringMap = mapOf(
        R.string.link_tg to stringResource(id = R.string.link_tg),
        R.string.bot_name to stringResource(id = R.string.bot_name),
    )
    TelegramLink(
        innerPadding = PaddingValues(),
        token = "thisToken",
        onLink = {},
        onCopy = {},
        getString = { stringMap[it]!! })
}

