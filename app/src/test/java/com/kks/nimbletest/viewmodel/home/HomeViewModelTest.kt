package com.kks.nimbletest.viewmodel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.fake_repo.FakeHomeRepository
import com.kks.nimbletest.repo.home.HomeRepo
import com.kks.nimbletest.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: HomeViewModel
    private lateinit var homeRepo: HomeRepo

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        homeRepo = FakeHomeRepository()
        sut = HomeViewModel(homeRepo,testCoroutineDispatcher)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `fetch survey list with invalid pageNumber or pageSize, return error`() {
        // Given
        val pageNumber = 0
        val pageSize = -1

        // When
        sut.getSurveyList(pageNumber,pageSize)

        // Then
        val result = sut.surveyListLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(ResourceState.GenericError(404,null))
    }

    @Test
    fun `fetch survey list with valid pageNumber or pageSize, return success`() {
        // Given
        val pageNumber = 1
        val pageSize = 1

        // When
        sut.getSurveyList(pageNumber,pageSize)

        // Then
        val result = sut.surveyListLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(ResourceState.Success(TestConstants.surveyResponse))
    }

    @Test
    fun `fetch user detail, return success`() {
        // Given

        // When
        sut.getUserDetail()

        // Then
        val result = sut.userLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(ResourceState.Success(TestConstants.userResponse))
    }
}