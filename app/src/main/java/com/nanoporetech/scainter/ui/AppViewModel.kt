package com.nanoporetech.scainter.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.data.AppUiState
import com.nanoporetech.scainter.network.ApiServiceRepository
import com.nanoporetech.scainter.network.LoginResult
import com.nanoporetech.scainter.network.NetworkApiRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface UiEvent {
    object Success: UiEvent
    data class Error(@StringRes val errorId: Int): UiEvent
}

class AppViewModel(
    private val repository: ApiServiceRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun login() {
        viewModelScope.launch {
            when(
              repository.login(
                  username = username,
                  password = password
              )
            ) {
                LoginResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            isLoginError = false
                        )
                    }
                    _events.emit(UiEvent.Success)
                }
                LoginResult.InvalidCredentials -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isLoginError = true
                        )
                    }
                    _events.emit(UiEvent.Error(R.string.err_invalid_credentials))
                }
                LoginResult.NetworkError -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isLoginError = false
                        )
                    }
                    _events.emit(UiEvent.Error(R.string.err_connection_offline))
                }

                LoginResult.UnknownError -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isLoginError = false
                        )
                    }
                    _events.emit(UiEvent.Error(R.string.err_unknown_error))
                }
            }
        }
    }
}

class AppViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(
                repository = NetworkApiRepository()
        ) as T
    }
}