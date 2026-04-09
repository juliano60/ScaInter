package com.nanoporetech.scainter.ui

import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.network.FakeApiRepository
import com.nanoporetech.scainter.network.LoginResult
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

    val fakeRepository = FakeApiRepository()
    val model: AppViewModel = AppViewModel(
        repository = fakeRepository,
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
        model.username = "620"
        model.password = "admin1"

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

    }

    @Test
    fun login_WhenInvalidCredentials_SetsLoginErrorFlag() = runTest {
        // arrange
        fakeRepository.loginResult = LoginResult.InvalidCredentials
        model.username = "620"
        model.password = "admin1"

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
        model.username = "620"
        model.password = "admin1"

        // act
        model.login()
        advanceUntilIdle()

        // assert
        val uiState = model.uiState.value
        assertFalse(uiState.isLoggedIn)
        assertFalse(uiState.isLoginError)
    }
}