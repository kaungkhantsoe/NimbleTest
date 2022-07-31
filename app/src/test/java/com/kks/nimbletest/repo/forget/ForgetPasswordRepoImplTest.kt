package com.kks.nimbletest.repo.forget

import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.FakeCustomKeyGenerator
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.util.MockResponseFileReader
import com.kks.nimbletest.util.MockitoHelper
import com.kks.nimbletest.util.success
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Response
import retrofit2.mock.Calls

class ForgetPasswordRepoImplTest {

    private lateinit var apiInterface: ApiInterface

    private lateinit var sut: ForgetPasswordRepoImpl

    @Before
    fun setup() {
        apiInterface = mock(ApiInterface::class.java)
        sut = ForgetPasswordRepoImpl(apiInterface, FakeCustomKeyGenerator())
    }

    @After
    fun tearDown() {}

    @Test
    fun `send forget password with invalid client_secret or client_id, return error`() {
        runBlocking {
            // Given
            val responseBody = MockResponseFileReader("forget_password_invalid_client.json").asResponseBody
            val response = Response.error<BaseResponse<String?>>(403, responseBody)
            Mockito.`when`(apiInterface.sendForgetPasswordMail(MockitoHelper.anyObject()))
                .thenReturn(Calls.response(response))

            val email = "my@gmail.com"

            // When
            val result = sut.sendForgetPasswordEmail(email).take(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.GenericError(403, "invalid_client"))
        }
    }

    @Test
    fun `send forget password with client_secret or client_id, return success`() {
        runBlocking {
            // Given
            val response = Response.success<BaseResponse<String?>>(
                200,
                TestConstants.baseForgetPasswordResponse
            )
            Mockito.`when`(apiInterface.sendForgetPasswordMail(MockitoHelper.anyObject()))
                .thenReturn(Calls.response(response))

            val email = "my@gmail.com"

            // When
            val result = sut.sendForgetPasswordEmail(email)
                .take(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.Success(success))
        }
    }
}
