package com.kks.nimbletest.repo.home

import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.data.network.reponse.UserResponse
import com.kks.nimbletest.util.executeOrThrow
import com.kks.nimbletest.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

class HomeRepoImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : HomeRepo {

    override fun fetchSurveyList(
        pageNumber: Int,
        pageSize: Int
    ): Flow<ResourceState<List<SurveyResponse>>> = flow {
        emit(ResourceState.Loading)

        val apiResult = safeApiCall(Dispatchers.IO) {
            apiInterface.getSurveyList(pageNumber, pageSize).executeOrThrow()
        }

        when (apiResult) {
            is ResourceState.Success -> {
                apiResult.successData?.data?.let {
                    if (it.isEmpty())
                        emit(ResourceState.EndReach)
                    else
                        emit(ResourceState.Success(it))

                } ?: emit(ResourceState.Error(AppConstants.SUCCESS_WITH_NULL_ERROR))
            }
            is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
            is ResourceState.GenericError -> emit(
                ResourceState.GenericError(
                    apiResult.code,
                    apiResult.error
                )
            )
            is ResourceState.ProtocolError -> emit(ResourceState.ProtocolError)
            else -> {
                emit(ResourceState.NetworkError)
            }
        }
    }.catch { error ->
        emit(ResourceState.Error(error.message ?: AppConstants.UNKNOWN_ERROR_MESSAGE))
    }

    override fun fetchUserDetail(): Flow<ResourceState<UserResponse>> =
        flow {
            emit(ResourceState.Loading)

            val apiResult = safeApiCall(Dispatchers.IO) {
                apiInterface.getUserDetail().executeOrThrow()
            }

            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.successData?.data?.let {
                        emit(ResourceState.Success(it))
                    } ?: emit(ResourceState.Error(AppConstants.SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                is ResourceState.GenericError -> emit(
                    ResourceState.GenericError(
                        apiResult.code,
                        apiResult.error
                    )
                )
                is ResourceState.ProtocolError -> emit(ResourceState.ProtocolError)
                else -> {
                    emit(ResourceState.NetworkError)
                }
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message ?: AppConstants.UNKNOWN_ERROR_MESSAGE))
        }
}