package com.kks.nimbletest.repo.token

import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.FakeCustomKeyGenerator
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.util.MockResponseFileReader
import com.kks.nimbletest.util.PreferenceManager
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.mock.Calls

@ExperimentalCoroutinesApi
class TokenRepoImplTest {

    private lateinit var apiInterface: ApiInterface
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var sut: TokenRepoImpl

    @Before
    fun setup() {
        apiInterface = mockk()
        preferenceManager = mockk()
        sut = TokenRepoImpl(apiInterface, preferenceManager, FakeCustomKeyGenerator())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `refresh token with invalid refresh token return invalid_grant error, do not call setStringData from PrefManager`() {
        runBlocking {
            // Given
            val responseBody =
                MockResponseFileReader("refresh_token_invalid_token.json").asResponseBody
            val response = Response.error<BaseResponse<LoginResponse>>(400, responseBody)
            every {
                apiInterface.refreshToken(any())
            } returns Calls.response(response)

            val refreshToken = "invalid"

            // When
            val result = sut.refreshToken(refreshToken).take(1).first()

            // Then
            // Verify mock function is not called
            assertThat(result is ResourceState.GenericError).isTrue()
            verify(inverse = true) { preferenceManager.setStringData(any(), any()) }
        }
    }

    @Test
    fun `refresh token with invalid email or password return invalid_grant error, do not call setStringData from PrefManager`() {
        runBlocking {
            // Given
            val responseBody =
                MockResponseFileReader("refresh_token_invalid_client_credential.json").asResponseBody
            val response = Response.error<BaseResponse<LoginResponse>>(401, responseBody)
            every {
                apiInterface.refreshToken(any())
            } returns Calls.response(response)

            val refreshToken = "validRefreshToken"

            // When
            val result = sut.refreshToken(refreshToken).take(1).first()

            // Then
            // Verify mock function is not called
            assertThat(result is ResourceState.GenericError).isTrue()
            verify(inverse = true) { preferenceManager.setStringData(any(), any()) }
        }
    }

    @Test
    fun `refresh token with valid email password token return success, setStringData from PrefManager is called`() {
        runBlocking {
            // Given
            val response =
                Response.success<BaseResponse<LoginResponse>>(
                    200,
                    TestConstants.baseLoginResponse
                )
            every {
                apiInterface.refreshToken(any())
            } returns Calls.response(response)

            every {
                preferenceManager.setStringData(any(), any())
                preferenceManager.setBooleanData(any(), any())
            } returns Unit

            val refreshToken = "validRefreshToken"

            // When
            val result = sut.refreshToken(refreshToken).take(1).first()

            // Then
            // Verify mock function is called
            assertThat(result is ResourceState.Success).isTrue()
            verify { preferenceManager.setStringData(any(), any()) }
        }
    }
}
