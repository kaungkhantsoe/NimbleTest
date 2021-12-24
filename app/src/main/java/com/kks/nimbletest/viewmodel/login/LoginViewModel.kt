package com.kks.nimbletest.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.di.IoDispatcher
import com.kks.nimbletest.repo.login.LoginRepo
import com.kks.nimbletest.util.error_email_empty
import com.kks.nimbletest.util.error_password_empty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _loginLiveData = MutableLiveData<ResourceState<LoginResponse>>()
    val loginLiveData: LiveData<ResourceState<LoginResponse>> = _loginLiveData

    fun login(email: String, password: String) {
        viewModelScope.launch(ioDispatcher) {

            _loginLiveData.postValue(ResourceState.Loading)
            when {
                email.isEmpty() -> _loginLiveData.postValue(
                    ResourceState.Error(
                        error_email_empty
                    )
                )
                password.isEmpty() -> _loginLiveData.postValue(
                    ResourceState.Error(
                        error_password_empty
                    )
                )
                else ->
                    loginRepo.loginWithEmailAndPassword(email, password).collect {
                        _loginLiveData.postValue(it)
                    }
            }

        }
    }

}