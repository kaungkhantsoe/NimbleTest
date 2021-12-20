package com.kks.nimbletest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.fake_repo.FakeLoginRepository
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.repo.login.LoginRepo
import com.kks.nimbletest.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeLoginRepo: LoginRepo

    private lateinit var sut: LoginViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        fakeLoginRepo = FakeLoginRepository()
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login with invalid email or password, return invalid_grant error`() {
        sut = LoginViewModel(fakeLoginRepo, testCoroutineDispatcher)

        // Given
        val email = ""
        val password = ""
        (fakeLoginRepo as FakeLoginRepository).client_id = "client_id"
        (fakeLoginRepo as FakeLoginRepository).client_secret = "client_secret"

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        if (result is ResourceState.GenericError)
            assertThat(result.error).isEqualTo("invalid_grant")
        else throw Exception("Should return error. But returning $result")
    }

    @Test
    fun `login with invalid client_id or client_secret, return invalid_client error`() {
        sut = LoginViewModel(fakeLoginRepo, testCoroutineDispatcher)

        // Given
        val email = "email"
        val password = "password"
        (fakeLoginRepo as FakeLoginRepository).client_id = ""
        (fakeLoginRepo as FakeLoginRepository).client_secret = ""

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        if (result is ResourceState.GenericError)
            assertThat(result.error).isEqualTo("invalid_client")
        else throw Exception("Should return error. But returning $result")
    }

    @Test
    fun `login with valid data, return success`() {
        sut = LoginViewModel(fakeLoginRepo, testCoroutineDispatcher)

        // Given
        val email = "email"
        val password = "password"
        (fakeLoginRepo as FakeLoginRepository).client_id = "client_id"
        (fakeLoginRepo as FakeLoginRepository).client_secret = "client_secret"

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        if (result is ResourceState.Success)
            assertThat(result.successData).isEqualTo(TestConstants.loginResponse)
        else throw Exception("Should return Success. But returning $result")
    }
}