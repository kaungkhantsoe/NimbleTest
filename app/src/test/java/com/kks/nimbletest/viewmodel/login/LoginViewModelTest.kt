package com.kks.nimbletest.viewmodel.login

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
        sut = LoginViewModel(fakeLoginRepo, testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login with invalid email or password, return invalid_grant error`() {
        // Given
        val email = ""
        val password = ""
        (fakeLoginRepo as FakeLoginRepository).client_id = "client_id"
        (fakeLoginRepo as FakeLoginRepository).client_secret = "client_secret"

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        assertThat (result).isEqualTo(ResourceState.GenericError(400,"invalid_grant"))
    }

    @Test
    fun `login with invalid client_id or client_secret, return invalid_client error`() {
        // Given
        val email = "email"
        val password = "password"
        (fakeLoginRepo as FakeLoginRepository).client_id = ""
        (fakeLoginRepo as FakeLoginRepository).client_secret = ""

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        assertThat (result).isEqualTo(ResourceState.GenericError(403,"invalid_client"))
    }

    @Test
    fun `login with valid data, return success`() {
        // Given
        val email = "email"
        val password = "password"
        (fakeLoginRepo as FakeLoginRepository).client_id = "client_id"
        (fakeLoginRepo as FakeLoginRepository).client_secret = "client_secret"

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        assertThat (result).isEqualTo(ResourceState.Success(TestConstants.loginResponse))
    }
}