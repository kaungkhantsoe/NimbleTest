package com.kks.nimbletest.viewmodel.forget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.fake_repo.FakeForgetPasswordRepository
import com.kks.nimbletest.repo.forget.ForgetPasswordRepo
import com.kks.nimbletest.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ForgetPasswordViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var fakeForgetPasswordRepo: ForgetPasswordRepo

    private lateinit var sut: ForgetPasswordViewModel

    @Before
    fun setup() {
        fakeForgetPasswordRepo = FakeForgetPasswordRepository()
        sut = ForgetPasswordViewModel(fakeForgetPasswordRepo,testCoroutineDispatcher)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `send forget password with invalid client_id or client_secret, return error`() {
        // Given
        val email = "email"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).client_id = ""
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).client_secret = ""

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result =  sut.forgetPasswordLiveData.getOrAwaitValue()
        Truth.assertThat(result).isEqualTo(ResourceState.GenericError(403,"invalid_client"))
    }

    @Test
    fun `send forget password with invalid email,return error`() {
        // Given
        val email = ""
        val password = ""
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).client_id = "client_id"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).client_secret = "client_secret"

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result =  sut.forgetPasswordLiveData.getOrAwaitValue()
        Truth.assertThat((result as ResourceState.Error).error).isEqualTo(AppConstants.error_email_empty)
    }

    @Test
    fun  `send forget password with valid data, return success`() {
        // Given
        val email = "email"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).client_id = "client_id"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).client_secret = "client_secret"

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result =  sut.forgetPasswordLiveData.getOrAwaitValue()
        Truth.assertThat(result).isEqualTo(ResourceState.Success(AppConstants.success))
    }
}