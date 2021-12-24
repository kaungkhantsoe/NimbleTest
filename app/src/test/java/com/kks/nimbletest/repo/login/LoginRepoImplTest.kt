package com.kks.nimbletest.repo.login

import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.FakeCustomKeyGenerator
import com.kks.nimbletest.util.MockResponseFileReader
import com.kks.nimbletest.util.MockitoHelper.anyObject
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.util.PreferenceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
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

    @Before
    fun setup() {
        preferenceManager = mock(PreferenceManager::class.java)
        apiInterface = mock(ApiInterface::class.java)
        sut = LoginRepoImpl(apiInterface, preferenceManager, FakeCustomKeyGenerator())
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
    fun `login with invalid password, return invalid grant_error`() {
        runBlocking {
            // Given
            val responseBody = MockResponseFileReader("login_invalid_grant.json").asResponseBody
            val response = Response.error<BaseResponse<LoginResponse>>(400, responseBody)
            `when`(apiInterface.loginUser(anyObject())).thenReturn(Calls.response(response))

            val password = "invalid"
            val email = "my@gmail.com"

            // When
            val result = sut.loginWithEmailAndPassword(email, password).take(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.GenericError(400,"invalid_grant"))
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
            val result = sut.loginWithEmailAndPassword(email, password).take(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.GenericError(400,"invalid_grant"))

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
            val result = sut.loginWithEmailAndPassword(email, password).take(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.GenericError(403,"invalid_client"))
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
            val result = sut.loginWithEmailAndPassword(email, password)
                .take(1).first()

            // Then
            verify(preferenceManager, times(2)).setStringData(anyObject(), anyObject())
            assertThat(result).isEqualTo(ResourceState.Success(response.body()!!.data))
        }
    }
}