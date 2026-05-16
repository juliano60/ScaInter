package com.nanoporetech.scainter.ui

import com.nanoporetech.scainter.credentials.RememberedCredentials
import com.nanoporetech.scainter.data.FetchProviderResult
import com.nanoporetech.scainter.ui.fake.FakeCredentialsStore
import com.nanoporetech.scainter.ui.fake.FakeDataSource
import com.nanoporetech.scainter.ui.fake.FakeDeviceTokenRegistrar
import com.nanoporetech.scainter.ui.fake.FakeScaNetworkDataRepository
import com.nanoporetech.scainter.ui.rules.TestDispatcherRule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AppViewModelTest {
    @get:Rule
    val mainDispatcherRule = TestDispatcherRule()

    private val fakeRepository = FakeScaNetworkDataRepository()
    private val fakeCredentialsStore = FakeCredentialsStore()
    private val fakeTokenRegistrar = FakeDeviceTokenRegistrar()

    @Test
    fun appViewModel_loginSuccess_updatesUiState() = runTest {
        // arrange
        val expectedProvider = FakeDataSource.providers[0]
        val model: AppViewModel = AppViewModel(
            repository = fakeRepository,
            credentialsStore = fakeCredentialsStore,
            deviceTokenRegistrar = fakeTokenRegistrar
        )
        model.setUsername("user")
        model.setPassword("password")
        fakeRepository.loginResult = FetchProviderResult.Success(FakeDataSource.providers[0])

        // act
        model.login()
        advanceUntilIdle()

        // assert
        val uiState = model.uiState.value
        assertTrue(uiState.isLoggedIn)
        assertFalse(uiState.isLoginError)
        assertEquals(expectedProvider, uiState.provider)
    }

    @Test
    fun appViewModel_loginSuccess_withRememberMe_storesCredentials() = runTest {
        // arrange
        val model = AppViewModel(
            repository = fakeRepository,
            credentialsStore = fakeCredentialsStore,
            deviceTokenRegistrar = fakeTokenRegistrar
        )
        model.setUsername("user")
        model.setPassword("password")
        model.setRememberMe(true)
        fakeRepository.loginResult = FetchProviderResult.Success(FakeDataSource.providers[0])

        // act
        model.login()
        advanceUntilIdle()

        // assert
        assertTrue(model.uiState.value.isLoggedIn)
        val credentials = fakeCredentialsStore.loadCredentials()
        assertNotNull(credentials)
        assertEquals(RememberedCredentials(
            username = "user",
            password = "password"
        ), credentials)
    }

    @Test
    fun appViewModel_loginFails_withRememberMe_doesNotStoreCredentials() = runTest {
        // arrange
        val model = AppViewModel(
            repository = fakeRepository,
            credentialsStore = fakeCredentialsStore,
            deviceTokenRegistrar = fakeTokenRegistrar
        )
        model.setUsername("user")
        model.setPassword("password")
        model.setRememberMe(true)
        fakeRepository.loginResult = FetchProviderResult.AuthenticationFailed

        // act
        model.login()
        advanceUntilIdle()

        // assert
        assertFalse(model.uiState.value.isLoggedIn)
        assertTrue(model.uiState.value.isLoginError)
        assertNull(fakeCredentialsStore.loadCredentials())
    }

    @Test
    fun appViewModel_logout_withRememberMe_loadsStoredCredentials() = runTest {
        // arrange
        val model = AppViewModel(
            repository = fakeRepository,
            credentialsStore = fakeCredentialsStore,
            deviceTokenRegistrar = fakeTokenRegistrar
        )
        model.setUsername("user")
        model.setPassword("password")
        model.setRememberMe(true)
        fakeRepository.loginResult = FetchProviderResult.Success(FakeDataSource.providers[0])

        // act
        model.login()
        advanceUntilIdle()
        model.logout()
        advanceUntilIdle()

        // assert
        val expectedCredentials = RememberedCredentials(
            username = "user",
            password = "password"
        )
        val credentials = fakeCredentialsStore.loadCredentials()
        assertNotNull(credentials)
        assertEquals(expectedCredentials, credentials)
    }
}

