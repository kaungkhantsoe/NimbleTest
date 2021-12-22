package com.kks.nimbletest.fake_repo

import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.data.network.reponse.UserResponse
import com.kks.nimbletest.repo.home.HomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

class FakeHomeRepository : HomeRepo {
    override fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int
    ): Flow<ResourceState<List<SurveyResponse>>> =
        flow {
            emit(ResourceState.Loading)

            if (pageNumber < 1 || pageSize < 1)
                emit(ResourceState.GenericError(404, null))
            else
                emit(ResourceState.Success(TestConstants.baseSurveyResponse.data!!))
        }

    override fun fetchUserDetail(): Flow<ResourceState<UserResponse>> =
        flow {
            emit(ResourceState.Loading)

            emit(ResourceState.Success(TestConstants.baseUserResponse.data!!))
        }
}