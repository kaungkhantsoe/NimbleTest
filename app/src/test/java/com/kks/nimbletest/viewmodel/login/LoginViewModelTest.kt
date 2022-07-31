package com.kks.nimbletest.viewmodel.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.fake_repo.FakeLoginRepository
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.repo.login.LoginRepo
import com.kks.nimbletest.util.error_email_empty
import com.kks.nimbletest.util.error_password_empty
import com.kks.nimbletest.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
    fun `login with empty email, return email empty error`() {
        runBlocking {
            // Given
            val password = "password"
            val email = ""
            (fakeLoginRepo as FakeLoginRepository).clientId = "client_id"
            (fakeLoginRepo as FakeLoginRepository).clientSecret = "client_secret"

            // When
            sut.login(email, password)

            // Then
            val result =  sut.loginLiveData.getOrAwaitValue()
            assertThat((result as ResourceState.Error).error).isEqualTo(error_email_empty)
        }
    }

    @Test
    fun `login with empty password, return password empty error`() {
        runBlocking {
            // Given
            val password = ""
            val email = "my@gmail.com"
            (fakeLoginRepo as FakeLoginRepository).clientId = "client_id"
            (fakeLoginRepo as FakeLoginRepository).clientSecret = "client_secret"

            // When
            sut.login(email, password)

            // Then
            val result =  sut.loginLiveData.getOrAwaitValue()
            assertThat((result as ResourceState.Error).error).isEqualTo(error_password_empty)
        }
    }

    @Test
    fun `login with invalid email or password, return invalid_grant error`() {
        // Given
        val email = "invalid"
        val password = "invalid"
        (fakeLoginRepo as FakeLoginRepository).clientId = "client_id"
        (fakeLoginRepo as FakeLoginRepository).clientSecret = "client_secret"

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
        (fakeLoginRepo as FakeLoginRepository).clientId = ""
        (fakeLoginRepo as FakeLoginRepository).clientSecret = ""

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
        (fakeLoginRepo as FakeLoginRepository).clientId = "client_id"
        (fakeLoginRepo as FakeLoginRepository).clientSecret = "client_secret"

        // When
        sut.login(email, password)

        // Then
        val result =  sut.loginLiveData.getOrAwaitValue()
        assertThat (result).isEqualTo(ResourceState.Success(TestConstants.loginResponse))
    }
}
