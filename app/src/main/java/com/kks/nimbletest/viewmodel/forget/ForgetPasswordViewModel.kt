package com.kks.nimbletest.viewmodel.forget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.di.IoDispatcher
import com.kks.nimbletest.repo.forget.ForgetPasswordRepo
import com.kks.nimbletest.util.error_email_empty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val homeRepo: ForgetPasswordRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _forgetPasswordLiveData = MutableLiveData<ResourceState<String>>()
    val forgetPasswordLiveData: LiveData<ResourceState<String>> = _forgetPasswordLiveData

    fun sendForgetPasswordEmail(email: String) {
        viewModelScope.launch(ioDispatcher) {
            _forgetPasswordLiveData.postValue(ResourceState.Loading)
            if (email.isEmpty()) {
                _forgetPasswordLiveData.postValue(ResourceState.Error(error_email_empty))
            } else {
                homeRepo.sendForgetPasswordEmail(email).collect {
                    _forgetPasswordLiveData.postValue(it)
                }
            }
        }
    }
}