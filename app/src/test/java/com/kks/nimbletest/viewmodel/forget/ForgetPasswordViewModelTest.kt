package com.kks.nimbletest.viewmodel.forget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.fake_repo.FakeForgetPasswordRepository
import com.kks.nimbletest.repo.forget.ForgetPasswordRepo
import com.kks.nimbletest.util.error_email_empty
import com.kks.nimbletest.util.getOrAwaitValue
import com.kks.nimbletest.util.success
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
        sut = ForgetPasswordViewModel(fakeForgetPasswordRepo, testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `send forget password with empty email, return error`() {
        // Given
        val email = ""
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientId = ""
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientSecret = ""

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result = sut.forgetPasswordLiveData.getOrAwaitValue()
        assertThat((result as ResourceState.Error).error).isEqualTo(error_email_empty)
    }

    @Test
    fun `send forget password with invalid client_id or client_secret, return error`() {
        // Given
        val email = "email"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientId = ""
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientSecret = ""

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result = sut.forgetPasswordLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(ResourceState.GenericError(403, "invalid_client"))
    }

    @Test
    fun `send forget password with invalid email,return error`() {
        // Given
        val email = "invalid"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientId = "client_id"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientSecret = "client_secret"

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result = sut.forgetPasswordLiveData.getOrAwaitValue()
        assertThat((result as ResourceState.GenericError).error).isEqualTo("invalid_client")
    }

    @Test
    fun `send forget password with valid data, return success`() {
        // Given
        val email = "email"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientId = "client_id"
        (fakeForgetPasswordRepo as FakeForgetPasswordRepository).clientSecret = "client_secret"

        // When
        sut.sendForgetPasswordEmail(email)

        // Then
        val result = sut.forgetPasswordLiveData.getOrAwaitValue()
        assertThat(result).isEqualTo(ResourceState.Success(success))
    }
}
