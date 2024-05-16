package com.zhulin.fakedet.ui.screens.authorization

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.zhulin.fakedet.R
import com.zhulin.fakedet.ui.navigation.models.Screen
import com.zhulin.fakedet.ui.screens.authorization.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.authorization.states.ScreenStateEnum
import com.zhulin.fakedet.ui.screens.create_post.components.Keyboard
import com.zhulin.fakedet.ui.screens.create_post.components.keyboardAsState
import com.zhulin.fakedet.ui.theme.AppBlack
import com.zhulin.fakedet.ui.theme.AppBlue
import com.zhulin.fakedet.ui.theme.AppRed
import com.zhulin.fakedet.ui.theme.AppWhite
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun AuthorizationScreen(
    navController: NavHostController,
    viewModel: AuthorizationViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val screenState = viewModel.screenState.value
    val signInState = viewModel.signInState.value
    val signUpState = viewModel.signUpState.value
    val restoreState = viewModel.restoreState.value

    val keyboardStatus by keyboardAsState()

    LaunchedEffect(key1 = true) {
        viewModel(ScreenEvent.Auth)
    }

    BackHandler {
        activity?.finish()
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AuthorizationViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            event.message,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                AuthorizationViewModel.UiEvent.EnterAccount -> {
                    navController.navigate(Screen.MainScreen.route)
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        when (screenState.state) {
            ScreenStateEnum.Loading -> {
                Loading(innerPadding = innerPadding)
            }

            ScreenStateEnum.SignIn -> {
                SignIn(
                    innerPadding = innerPadding,
                    loginValue = signInState.login,
                    onLoginValueChange = { viewModel(ScreenEvent.UpdateInLogin(it)) },
                    passwordValue = signInState.password,
                    onPasswordValueChange = { viewModel(ScreenEvent.UpdateInPassword(it)) },
                    onForget = { viewModel(ScreenEvent.UpdateScreenState(ScreenStateEnum.Restore)) },
                    onSignIn = { viewModel(ScreenEvent.SignIn) },
                    onAccount = { viewModel(ScreenEvent.UpdateScreenState(ScreenStateEnum.SignUp)) },
                    keyboardOpen = keyboardStatus == Keyboard.Opened,
                    getString = { id -> viewModel.getStringResource(id) })
            }

            ScreenStateEnum.SignUp -> {
                SignUp(
                    innerPadding = innerPadding,
                    nameValue = signUpState.name,
                    onNameValueChange = { viewModel(ScreenEvent.UpdateUpName(it)) },
                    loginValue = signUpState.login,
                    onLoginValueChange = { viewModel(ScreenEvent.UpdateUpLogin(it)) },
                    passwordValue = signUpState.password,
                    onPasswordValueChange = { viewModel(ScreenEvent.UpdateUpPassword(it)) },
                    onSignUp = { viewModel(ScreenEvent.SignUp) },
                    onAccount = { viewModel(ScreenEvent.UpdateScreenState(ScreenStateEnum.SignIn)) },
                    keyboardOpen = keyboardStatus == Keyboard.Opened,
                    getString = { id -> viewModel.getStringResource(id) }
                )
            }

            ScreenStateEnum.Restore -> {
                RestorePassword(
                    innerPadding = innerPadding,
                    loginValue = restoreState.login,
                    onLoginValueChange = { viewModel(ScreenEvent.UpdateRestoreLogin(it)) },
                    passwordValue = restoreState.password,
                    onPasswordValueChange = { viewModel(ScreenEvent.UpdateRestorePassword(it)) },
                    onRestore = { viewModel(ScreenEvent.Restore) },
                    onAccount = { viewModel(ScreenEvent.UpdateScreenState(ScreenStateEnum.SignIn)) },
                    keyboardOpen = keyboardStatus == Keyboard.Opened,
                    getString = { id -> viewModel.getStringResource(id) }
                )
            }
        }

    }
}

@ExperimentalMaterial3Api
@Composable
private fun Loading(
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(AppWhite)
            .padding(horizontal = 60.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun LoadingPreview() {
    Loading(
        innerPadding = PaddingValues()
    )
}

@ExperimentalMaterial3Api
@Composable
private fun SignIn(
    innerPadding: PaddingValues,
    loginValue: String,
    onLoginValueChange: (it: String) -> Unit,
    passwordValue: String,
    onPasswordValueChange: (it: String) -> Unit,
    onSignIn: () -> Unit,
    onForget: () -> Unit,
    onAccount: () -> Unit,
    keyboardOpen: Boolean,
    getString: (id: Int) -> String
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(AppWhite)
            .padding(horizontal = 60.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(60.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = AppRed)) {
                    append("Fake")
                }
                withStyle(style = SpanStyle(color = AppBlack)) {
                    append("DET")
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = getString(R.string.auth_sign_in_welcome),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 32.sp,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = getString(R.string.auth_sign_in_welcome_placeholder),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = loginValue,
                onValueChange = onLoginValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_login_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                )
            )

            TextField(
                value = passwordValue,
                onValueChange = onPasswordValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_password_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            Text(
                text = getString(R.string.auth_sign_in_forgot),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .clickable {
                        onForget()
                    },
                textAlign = TextAlign.Right
            )
        }

        if (!keyboardOpen) {
            Button(
                onClick = onSignIn,
                colors = ButtonDefaults.buttonColors(containerColor = AppBlack),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = getString(R.string.auth_sign_in),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    ),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onAccount,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = AppBlack)) {
                            append(getString(R.string.auth_sign_in_account))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(color = AppBlue)) {
                            append(getString(R.string.auth_sign_in_sign_up))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SingInPreview() {
    val stringMap = mapOf(
        R.string.auth_sign_in_account to stringResource(id = R.string.auth_sign_in_account),
        R.string.auth_sign_in_welcome to stringResource(id = R.string.auth_sign_in_welcome),
        R.string.auth_sign_in_welcome_placeholder to stringResource(id = R.string.auth_sign_in_welcome_placeholder),
        R.string.auth_sign_in_sign_up to stringResource(id = R.string.auth_sign_in_sign_up),
        R.string.auth_sign_in_forgot to stringResource(id = R.string.auth_sign_in_forgot),
        R.string.auth_login to stringResource(id = R.string.auth_login),
        R.string.auth_password to stringResource(id = R.string.auth_password),
        R.string.auth_password_placeholder to stringResource(id = R.string.auth_password_placeholder),
        R.string.auth_sign_in to stringResource(id = R.string.auth_sign_in)
    )
    SignIn(
        innerPadding = PaddingValues(),
        loginValue = stringResource(id = R.string.auth_login),
        onLoginValueChange = {},
        passwordValue = stringResource(id = R.string.auth_password),
        onPasswordValueChange = {},
        onSignIn = {},
        onForget = {},
        onAccount = {},
        keyboardOpen = false,
        getString = { id -> stringMap[id]!! }
    )
}

@ExperimentalMaterial3Api
@Composable
private fun SignUp(
    innerPadding: PaddingValues,
    nameValue: String,
    onNameValueChange: (it: String) -> Unit,
    loginValue: String,
    onLoginValueChange: (it: String) -> Unit,
    passwordValue: String,
    onPasswordValueChange: (it: String) -> Unit,
    onSignUp: () -> Unit,
    onAccount: () -> Unit,
    keyboardOpen: Boolean,
    getString: (id: Int) -> String
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(AppWhite)
            .padding(horizontal = 60.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(60.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = AppRed)) {
                    append("Fake")
                }
                withStyle(style = SpanStyle(color = AppBlack)) {
                    append("DET")
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = getString(R.string.auth_sign_up_welcome),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 32.sp,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = getString(R.string.auth_sign_up_welcome_placeholder),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = nameValue,
                onValueChange = onNameValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_name_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                )
            )

            TextField(
                value = loginValue,
                onValueChange = onLoginValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_login_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                )
            )

            TextField(
                value = passwordValue,
                onValueChange = onPasswordValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_password_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )
        }

        if (!keyboardOpen) {
            Button(
                onClick = onSignUp,
                colors = ButtonDefaults.buttonColors(containerColor = AppBlack),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = getString(R.string.auth_sign_up),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    ),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onAccount,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = AppBlack)) {
                            append(getString(R.string.auth_sign_up_account))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(color = AppBlue)) {
                            append(getString(R.string.auth_sign_up_sign_in))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SingUpPreview() {
    val stringMap = mapOf(
        R.string.auth_sign_up_welcome to stringResource(id = R.string.auth_sign_up_welcome),
        R.string.auth_sign_up_welcome_placeholder to stringResource(id = R.string.auth_sign_up_welcome_placeholder),
        R.string.auth_sign_up to stringResource(id = R.string.auth_sign_up),
        R.string.auth_sign_up_account to stringResource(id = R.string.auth_sign_up_account),
        R.string.auth_sign_up_sign_in to stringResource(id = R.string.auth_sign_up_sign_in),
    )
    SignUp(
        innerPadding = PaddingValues(),
        nameValue = stringResource(id = R.string.auth_name),
        onNameValueChange = {},
        loginValue = stringResource(id = R.string.auth_login),
        onLoginValueChange = {},
        passwordValue = stringResource(id = R.string.auth_password),
        onPasswordValueChange = {},
        onSignUp = {},
        onAccount = {},
        keyboardOpen = false,
        getString = { id -> stringMap[id]!! }
    )
}


@ExperimentalMaterial3Api
@Composable
private fun RestorePassword(
    innerPadding: PaddingValues,
    loginValue: String,
    onLoginValueChange: (it: String) -> Unit,
    passwordValue: String,
    onPasswordValueChange: (it: String) -> Unit,
    onRestore: () -> Unit,
    onAccount: () -> Unit,
    keyboardOpen: Boolean,
    getString: (id: Int) -> String
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(AppWhite)
            .padding(horizontal = 60.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(60.dp)
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = AppRed)) {
                    append("Fake")
                }
                withStyle(style = SpanStyle(color = AppBlack)) {
                    append("DET")
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = getString(R.string.auth_sign_up_welcome),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 32.sp,
                    fontSize = 32.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = getString(R.string.auth_welcome_restore),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = loginValue,
                onValueChange = onLoginValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_login_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                )
            )

            TextField(
                value = passwordValue,
                onValueChange = onPasswordValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = AppBlack
                ),
                placeholder = {
                    Text(text = getString(R.string.auth_password_placeholder))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppBlack,
                    selectionColors = TextSelectionColors(
                        backgroundColor = AppBlue,
                        handleColor = AppBlack
                    ),
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        if (!keyboardOpen) {
            Button(
                onClick = onRestore,
                colors = ButtonDefaults.buttonColors(containerColor = AppBlack),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = getString(R.string.auth_restore),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    ),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onAccount,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = AppBlack)) {
                            append(getString(R.string.auth_sign_up_account))
                            append(" ")
                        }
                        withStyle(style = SpanStyle(color = AppBlue)) {
                            append(getString(R.string.auth_sign_up_sign_in))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun RestorePasswordPreview() {
    val stringMap = mapOf(
        R.string.auth_sign_in_account to stringResource(id = R.string.auth_sign_in_account),
        R.string.auth_sign_in_welcome to stringResource(id = R.string.auth_sign_in_welcome),
        R.string.auth_sign_in_welcome_placeholder to stringResource(id = R.string.auth_sign_in_welcome_placeholder),
        R.string.auth_sign_in_sign_up to stringResource(id = R.string.auth_sign_in_sign_up),
        R.string.auth_sign_in_forgot to stringResource(id = R.string.auth_sign_in_forgot),
        R.string.auth_login to stringResource(id = R.string.auth_login),
        R.string.auth_password to stringResource(id = R.string.auth_password),
        R.string.auth_password_placeholder to stringResource(id = R.string.auth_password_placeholder),
        R.string.auth_sign_in to stringResource(id = R.string.auth_sign_in),
        R.string.auth_restore to stringResource(id = R.string.auth_restore),
        R.string.auth_welcome_restore to stringResource(id = R.string.auth_welcome_restore),
        R.string.auth_sign_up_welcome to stringResource(id = R.string.auth_sign_up_welcome),
        R.string.auth_sign_up_account to stringResource(id = R.string.auth_sign_up_account),
        R.string.auth_sign_up_sign_in to stringResource(id = R.string.auth_sign_up_sign_in),
    )
    RestorePassword(
        innerPadding = PaddingValues(),
        loginValue = stringResource(id = R.string.auth_login),
        onLoginValueChange = {},
        passwordValue = stringResource(id = R.string.auth_password),
        onPasswordValueChange = {},
        onRestore = {},
        onAccount = {},
        keyboardOpen = false,
        getString = { id -> stringMap[id]!! }
    )
}