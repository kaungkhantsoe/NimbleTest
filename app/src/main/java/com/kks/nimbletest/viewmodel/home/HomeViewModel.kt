package com.kks.nimbletest.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.data.network.reponse.UserResponse
import com.kks.nimbletest.di.IoDispatcher
import com.kks.nimbletest.repo.home.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepo: HomeRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _userLiveData = MutableLiveData<ResourceState<UserResponse>>()
    val userLiveData: LiveData<ResourceState<UserResponse>> = _userLiveData

    private val _surveyListLiveData = MutableLiveData<ResourceState<List<SurveyResponse>>>()
    val surveyListLiveData: LiveData<ResourceState<List<SurveyResponse>>> = _surveyListLiveData

    fun getUserDetail() {
        viewModelScope.launch(ioDispatcher) {
            homeRepo.fetchUserDetail().collect {
                _userLiveData.postValue(it)
            }
        }
    }

    fun getSurveyList(pageNumber: Int, pageSize: Int) {
        viewModelScope.launch(ioDispatcher) {
            _surveyListLiveData.postValue(ResourceState.Loading)
            homeRepo.fetchSurveyList(pageNumber, pageSize).collect {
                _surveyListLiveData.postValue(it)
            }
        }
    }
}