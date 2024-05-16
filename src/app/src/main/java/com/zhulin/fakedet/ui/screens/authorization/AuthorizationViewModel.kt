package com.zhulin.fakedet.ui.screens.authorization

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.zhulin.fakedet.R
import com.zhulin.fakedet.business.usecases.UseCaseHandler
import com.zhulin.fakedet.ui.helpers.StringHelpers.claimUpdate
import com.zhulin.fakedet.ui.helpers.StringHelpers.claimValidate
import com.zhulin.fakedet.ui.screens.AbstractViewModel
import com.zhulin.fakedet.ui.screens.authorization.states.RestoreState
import com.zhulin.fakedet.ui.screens.authorization.states.ScreenEvent
import com.zhulin.fakedet.ui.screens.authorization.states.ScreenState
import com.zhulin.fakedet.ui.screens.authorization.states.ScreenStateEnum
import com.zhulin.fakedet.ui.screens.authorization.states.SignInState
import com.zhulin.fakedet.ui.screens.authorization.states.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val useCaseHandler: UseCaseHandler
) : AbstractViewModel(useCaseHandler) {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState


    private val _signInState = mutableStateOf(SignInState())
    val signInState: State<SignInState> = _signInState

    private val _signUpState = mutableStateOf(SignUpState())
    val signUpState: State<SignUpState> = _signUpState

    private val _restoreState = mutableStateOf(RestoreState())
    val restoreState: State<RestoreState> = _restoreState

    operator fun invoke(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.SignIn -> {
                if (!signInState.value.login.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_login_incorrect)))
                    }

                    return
                }
                if (!signInState.value.password.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_password_incorrect)))
                    }

                    return
                }

                viewModelScope.launch {
                    try {
                        val info = useCaseHandler.auth.login(
                            signInState.value.login,
                            signInState.value.password
                        )

                        if (!info.result || info.userName.isNullOrBlank() || info.token.isNullOrBlank()) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend)))

                            return@launch
                        }

                        useCaseHandler.auth.save(info.userName, info.token!!)

                        _eventFlow.emit(UiEvent.EnterAccount)
                    }
                    catch (ex: Exception){
                        _eventFlow.emit(UiEvent.ShowSnackbar(getStringResource(R.string.error_happend)))
                    }
                }
            }

            is ScreenEvent.SignUp -> {
                if (!signUpState.value.name.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_name_incorrect)))
                    }

                    return
                }

                if (!signUpState.value.login.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_login_incorrect)))
                    }

                    return
                }

                if (!signUpState.value.password.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_password_incorrect)))
                    }

                    return
                }

                viewModelScope.launch {
                    try {
                        val info = useCaseHandler.auth.register(
                            signUpState.value.login,
                            signUpState.value.name,
                            signUpState.value.password
                        )

                        if (!info.result || info.userName.isNullOrBlank() || info.token.isNullOrBlank()) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend)))

                            return@launch
                        }

                        useCaseHandler.auth.save(info.userName, info.token!!)

                        _eventFlow.emit(UiEvent.EnterAccount)
                    }
                    catch (ex: Exception){
                        _eventFlow.emit(UiEvent.ShowSnackbar(getStringResource(R.string.error_happend)))
                    }
                }
            }

            is ScreenEvent.Auth -> viewModelScope.launch {
                try {
                    val info = useCaseHandler.auth.auth()

                    if (!info.result || info.userName.isNullOrBlank() || info.token.isNullOrBlank()) {
                        _screenState.value = screenState.value.copy(
                            state = ScreenStateEnum.SignIn
                        )

                        return@launch
                    }

                    useCaseHandler.auth.save(info.userName, info.token!!)

                    _eventFlow.emit(UiEvent.EnterAccount)
                }
                catch (ex: Exception){
                    _eventFlow.emit(UiEvent.ShowSnackbar(getStringResource(R.string.error_happend)))
                }
            }

            is ScreenEvent.UpdateInLogin -> _signInState.value = signInState.value.copy(
                login = event.value.claimUpdate()
            )

            is ScreenEvent.UpdateInPassword -> _signInState.value = signInState.value.copy(
                password = event.value.claimUpdate()
            )

            is ScreenEvent.UpdateUpLogin -> _signUpState.value = signUpState.value.copy(
                login = event.value.claimUpdate()
            )

            is ScreenEvent.UpdateUpName -> _signUpState.value = signUpState.value.copy(
                name = event.value.claimUpdate()
            )

            is ScreenEvent.UpdateUpPassword -> _signUpState.value = signUpState.value.copy(
                password = event.value.claimUpdate()
            )

            is ScreenEvent.UpdateScreenState -> {
                _screenState.value = screenState.value.copy(
                    state = event.value
                )
            }

            is ScreenEvent.Restore -> {
                if (!restoreState.value.login.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_login_incorrect)))
                    }

                    return
                }
                if (!restoreState.value.password.claimValidate()) {
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.auth_password_incorrect)))
                    }

                    return
                }

                viewModelScope.launch {
                    try {
                        val info = useCaseHandler.auth.restorePassword(
                            restoreState.value.login,
                            restoreState.value.password
                        )

                        if (!info.result || info.userName.isNullOrBlank() || info.token.isNullOrBlank()) {
                            _eventFlow.emit(UiEvent.ShowSnackbar(message = getStringResource(R.string.error_happend)))

                            return@launch
                        }

                        useCaseHandler.auth.save(info.userName, info.token!!)

                        _eventFlow.emit(UiEvent.EnterAccount)
                    }
                    catch (ex: Exception){
                        _eventFlow.emit(UiEvent.ShowSnackbar(getStringResource(R.string.error_happend)))
                    }
                }
            }
            is ScreenEvent.UpdateRestoreLogin -> _restoreState.value = restoreState.value.copy(
                login = event.value
            )
            is ScreenEvent.UpdateRestorePassword -> _restoreState.value = restoreState.value.copy(
                password = event.value
            )
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object EnterAccount : UiEvent()
    }
}