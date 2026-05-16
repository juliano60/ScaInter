package com.nanoporetech.scainter.ui

import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.network.FakeApiRepository
import com.nanoporetech.scainter.network.LoginResult
import com.nanoporetech.scainter.notification.DeviceTokenRegistrar
import com.nanoporetech.scainter.ui.login.CredentialsStoreBase
import com.nanoporetech.scainter.ui.login.RememberedCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class AppViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeRepository = FakeApiRepository()
    private val fakeCredentialsStore = FakeCredentialsStore()
    private val fakeTokenRegistrar = FakeDeviceTokenRegistrar()
    private val model: AppViewModel = AppViewModel(
        repository = fakeRepository,
        credentialsStore = fakeCredentialsStore,
        deviceTokenRegistrar = fakeTokenRegistrar
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun login_WithValidCredentials_LogsInSuccessfully() = runTest {
        // arrange
        val provider = Provider(
            id = 620,
            role = "etablissement",
            name = "TestName"
        )
        fakeRepository.loginResult = LoginResult.Success(provider = provider)
        model.setUsername("620")
        model.setPassword("admin1")

        // act
        model.login()
        advanceUntilIdle()

        // assert
        val uiState = model.uiState.value
        assertTrue(uiState.isLoggedIn)
        assertFalse(uiState.isLoginError)
        assertEquals(provider.id, uiState.provider?.id)
        assertEquals(provider.name, uiState.provider?.name)
        assertEquals(provider.role, uiState.provider?.role)
        assertEquals(provider.id.toString(), fakeTokenRegistrar.lastRegisteredUserId)
    }

    @Test
    fun login_WhenInvalidCredentials_SetsLoginErrorFlag() = runTest {
        // arrange
        fakeRepository.loginResult = LoginResult.InvalidCredentials
        model.setUsername("620")
        model.setPassword("admin1")

        // act
        model.login()
        advanceUntilIdle()

        // assert
        val uiState = model.uiState.value
        assertFalse(uiState.isLoggedIn)
        assertTrue(uiState.isLoginError)
    }

    @Test
    fun login_WhenNetworkError_SetsLoginErrorFlag() = runTest {
        // arrange
        fakeRepository.loginResult = LoginResult.NetworkError
        model.setUsername("620")
        model.setPassword("admin1")

        // act
        model.login()
        advanceUntilIdle()

        // assert
        val uiState = model.uiState.value
        assertFalse(uiState.isLoggedIn)
        assertFalse(uiState.isLoginError)
    }
}

private class FakeCredentialsStore : CredentialsStoreBase {
    override fun saveCredentials(username: String, password: String) = Unit

    override fun clearCredentials() = Unit

    override fun loadCredentials(): RememberedCredentials? = null
}

private class FakeDeviceTokenRegistrar : DeviceTokenRegistrar {
    var lastRegisteredUserId: String? = null

    override suspend fun registerDeviceToken(userId: String) {
        lastRegisteredUserId = userId
    }
}
