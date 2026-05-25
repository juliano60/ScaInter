package com.nanoporetech.scainter.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.AppUiState
import com.nanoporetech.scainter.data.FetchProviderResult
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.notification.DeviceTokenRegistrar
import com.nanoporetech.scainter.notification.FirebaseDeviceTokenRegistrar
import com.nanoporetech.scainter.credentials.CredentialsStore
import com.nanoporetech.scainter.credentials.CredentialsStoreBase
import com.nanoporetech.scainter.data.FetchConsultationsResult
import com.nanoporetech.scainter.data.FetchExaminationsResult
import com.nanoporetech.scainter.model.Consultation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface UiEvent {
    object LoginSucceeded: UiEvent
    data class Error(@StringRes val errorId: Int): UiEvent
    object LoggedOut: UiEvent
}

class AppViewModel(
    private val repository: ScaDataRepository,
    private val credentialsStore: CredentialsStoreBase,
    private val deviceTokenRegistrar: DeviceTokenRegistrar
): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        loadCredentials()
    }

    private fun loadCredentials() {
        val credentials = credentialsStore.loadCredentials()
        if (credentials != null) {
            _uiState.update {
                it.copy(
                    username = credentials.username,
                    password = credentials.password,
                    rememberMe = true
                )
            }
        }
    }

    fun login() {
        viewModelScope.launch {
              when(val result = repository.fetchProvider(
                  username = _uiState.value.username,
                  password = _uiState.value.password)) {

                  is FetchProviderResult.Success -> {
                      _uiState.update {
                          it.copy(
                              isLoggedIn = true,
                              isLoginError = false,
                              provider = result.provider
                          )
                      }
                      val state = _uiState.value

                      if (state.rememberMe) {
                          credentialsStore.saveCredentials(state.username, state.password)
                      } else {
                          credentialsStore.clearCredentials()
                      }

                      deviceTokenRegistrar.registerDeviceToken(result.provider.id.toString())
                      _events.emit(UiEvent.LoginSucceeded)
                  }
                  is FetchProviderResult.AuthenticationFailed -> {
                      _uiState.update {
                          it.copy(
                              isLoggedIn = false,
                              isLoginError = true
                          )
                      }
                      _events.emit(UiEvent.Error(R.string.err_invalid_credentials))
                  }
                  is FetchProviderResult.NetworkError -> {
                      _uiState.update {
                          it.copy(
                              isLoggedIn = false,
                              isLoginError = false
                          )
                      }
                      _events.emit(UiEvent.Error(R.string.err_connection_offline))
                  }
              }
        }
    }

    suspend fun fetchConsultations(): Boolean {
        return when (val result = repository.fetchConsultationsFor(_uiState.value.provider.name)) {
            is FetchConsultationsResult.Success -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        consultations = result.consultations
                    )
                }
                true
            }
            is FetchConsultationsResult.NetworkError -> {
                _events.emit(UiEvent.Error(R.string.err_connection_offline))
                false
            }
            else -> {
                _events.emit(UiEvent.Error(R.string.err_unknown_error))
                false
            }
        }
    }

    suspend fun fetchExaminations(): Boolean {
        return when (val result = repository.fetchExaminationsFor(_uiState.value.provider.name)) {
            is FetchExaminationsResult.Success -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        examinations = result.examinations
                    )
                }
                true
            }
            is FetchExaminationsResult.NetworkError -> {
                _events.emit(UiEvent.Error(R.string.err_connection_offline))
                false
            }
            else -> {
                _events.emit(UiEvent.Error(R.string.err_unknown_error))
                false
            }
        }
    }

    fun reset() {
        _uiState.value = AppUiState()
        loadCredentials()
    }

    fun logout() {
        reset()

        viewModelScope.launch {
            _events.emit(UiEvent.LoggedOut)
        }
    }

    fun setRememberMe(newValue: Boolean) {
       _uiState.update { currentState ->
           currentState.copy(
               rememberMe = newValue
           )
       }

        if (!newValue) {
            credentialsStore.clearCredentials()
        }
    }

    fun setUsername(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = username
            )
        }
    }

    fun setPassword(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password
            )
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ScaInterApplication)
                val repository = application.container.scaDataRepository
                val credentialsStore = CredentialsStore(application.applicationContext)
                val deviceTokenRegistrar = FirebaseDeviceTokenRegistrar(application.applicationContext)
                AppViewModel(
                    repository = repository,
                    credentialsStore = credentialsStore,
                    deviceTokenRegistrar = deviceTokenRegistrar)
            }
        }
    }
}