package com.kks.nimbletest.repo.home

import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.data.network.reponse.UserResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

interface HomeRepo {
    fun fetchSurveyList(pageNumber: Int, pageSize: Int): Flow<ResourceState<List<SurveyResponse>>>
    fun fetchUserDetail(): Flow<ResourceState<UserResponse>>
}