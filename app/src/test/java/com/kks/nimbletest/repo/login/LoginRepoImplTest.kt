package com.kks.nimbletest.repo.login

import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.util.MockResponseFileReader
import com.kks.nimbletest.util.MockitoHelper.anyObject
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.util.PreferenceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response
import retrofit2.mock.Calls

@ExperimentalCoroutinesApi
class LoginRepoImplTest {

    private lateinit var apiInterface: ApiInterface
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var sut: LoginRepoImpl

//    @get: Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        preferenceManager = mock(PreferenceManager::class.java)
        apiInterface = mock(ApiInterface::class.java)
        sut = LoginRepoImpl(apiInterface, preferenceManager)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `read simple file`() {
        val reader = MockResponseFileReader("test.json")
        assertThat(reader.content).isEqualTo("success")
    }

    @Test
    fun `login with empty email, return email empty error`() {
        runBlocking {
            // Given
            val password = "password"
            val email = ""

            // When
            val secondResult = sut.loginWithEmailAndPassword(email, password).drop(1).first()

            // Then
            assertThat((secondResult as ResourceState.Error).error).isEqualTo(AppConstants.error_email_empty)
        }
    }

    @Test
    fun `login with empty password, return email empty error`() {
        runBlocking {
            // Given
            val password = ""
            val email = "my@gmail.com"

            // When
            val secondResult = sut.loginWithEmailAndPassword(email, password).drop(1).first()

            // Then
            assertThat((secondResult as ResourceState.Error).error).isEqualTo(AppConstants.error_password_empty)
        }
    }

    @Test
    fun `login with invalid password, return invalid grant_error`() {
        runBlocking {
            // Given
            val responseBody = MockResponseFileReader("login_invalid_grant.json").asResponseBody
            val response = Response.error<BaseResponse<LoginResponse>>(400, responseBody)
            `when`(apiInterface.loginUser(anyObject())).thenReturn(Calls.response(response))

            val password = "invalid"
            val email = "my@gmail.com"

            // When
            val secondResult = sut.loginWithEmailAndPassword(email, password).drop(1).first()

            // Then
            assertThat((secondResult as ResourceState.GenericError).error).isEqualTo("invalid_grant")
        }
    }

    @Test
    fun `login with invalid email, return invalid_grant error`() {
        runBlocking {
            // Given
            val responseBody = MockResponseFileReader("login_invalid_grant.json").asResponseBody
            val response = Response.error<BaseResponse<LoginResponse>>(400, responseBody)
            `when`(apiInterface.loginUser(anyObject())).thenReturn(Calls.response(response))

            val password = "password"
            val email = "invalid"

            // When
            val secondResult = sut.loginWithEmailAndPassword(email, password).drop(1).first()

            // Then
            assertThat((secondResult as ResourceState.GenericError).error).isEqualTo("invalid_grant")

        }
    }

    @Test
    fun `login with invalid client_id or client_secret, return invalid_client error`() {
        runBlocking {
            // Given
            val responseBody = MockResponseFileReader("login_invalid_client.json").asResponseBody
            val response = Response.error<BaseResponse<LoginResponse>>(403, responseBody)
            `when`(apiInterface.loginUser(anyObject())).thenReturn(Calls.response(response))

            val password = "password"
            val email = "my@gmail.com"

            // When
            val secondResult = sut.loginWithEmailAndPassword(email, password).drop(1).first()

            // Then
            assertThat((secondResult as ResourceState.GenericError).error).isEqualTo("invalid_client")
        }
    }

    @Test
    fun `login with valid client_id, client_secret, email and password, return success`() {
        runBlocking {
            // Given
            val response = Response.success<BaseResponse<LoginResponse>>(
                200,
                TestConstants.baseLoginResponse
            )
            `when`(apiInterface.loginUser(anyObject())).thenReturn(Calls.response(response))

            val password = "password"
            val email = "my@gmail.com"

            // When
            val secondResult = sut.loginWithEmailAndPassword(email, password)
                .drop(1).first()

            // Then
            verify(preferenceManager, times(2)).setStringData(anyObject(), anyObject())
            assertThat(secondResult).isEqualTo(ResourceState.Success(response.body()!!.data))
        }
    }
}