package com.kks.nimbletest.repo.home

import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.util.MockResponseFileReader
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
class HomeRepoImplTest {

    private lateinit var apiInterface: ApiInterface

    lateinit var sut: HomeRepoImpl

    @Before
    fun setup() {
        apiInterface = mock(ApiInterface::class.java)
        sut = HomeRepoImpl(apiInterface)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `fetch survey list with invalid pageNumber, return error`() {
        runBlocking {
            // Given
            val responseBody = MockResponseFileReader("survey_list_invalid_page_number.json").asResponseBody
            val response = Response.error<BaseResponse<List<SurveyResponse>>?>(404, responseBody)
            `when`(apiInterface.getSurveyList(anyInt(), anyInt()))
                .thenReturn(Calls.response(response))

            val pageNumber = 0
            val pageSize = 1

            // When
            val result = sut.fetchSurveyList(pageNumber, pageSize).drop(1).first()

            // Then
            assertThat(result is ResourceState.GenericError).isTrue()
        }
    }

    @Test
    fun `fetch survey list with valid pageNumber and pageSize, return success`() {
        runBlocking {
            // Given
            val response = Response.success<BaseResponse<List<SurveyResponse>>>(
                200,
                TestConstants.baseSurveyResponse
            )
            `when`(apiInterface.getSurveyList(anyInt(), anyInt()))
                .thenReturn(Calls.response(response))

            val pageNumber = 1
            val pageSize = 1

            // When
            val result = sut.fetchSurveyList(pageNumber, pageSize).drop(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.Success(response.body()!!.data))
        }
    }

    @Test
    fun `fetch user detail, return success`() {
        runBlocking {
            // Given
            val response = Response.success(200, TestConstants.baseUserResponse)
            `when`(apiInterface.getUserDetail()).thenReturn(Calls.response(response))

            // When
            val result = sut.fetchUserDetail().drop(1).first()

            // Then
            assertThat(result).isEqualTo(ResourceState.Success(response.body()!!.data))
        }
    }
}