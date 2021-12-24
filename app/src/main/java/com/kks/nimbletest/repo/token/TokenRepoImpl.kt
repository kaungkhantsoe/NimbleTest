package com.kks.nimbletest.repo.token

import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.data.network.request.RefreshTokenRequest
import com.kks.nimbletest.util.*
import com.kks.nimbletest.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class TokenRepoImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val preferenceManager: PreferenceManager,
    private val customKeyProvider: CustomKeyProvider
) : TokenRepo {

    override fun refreshToken(refreshToken: String): Flow<ResourceState<LoginResponse>> =
        flow {

            Timber.d("Start refreshing")
            val apiResult = safeApiCall(Dispatchers.IO) {
                apiInterface.refreshToken(
                    RefreshTokenRequest(
                        refreshToken = refreshToken,
                        clientId = customKeyProvider.getClientId(),
                        clientSecret = customKeyProvider.getClientSecret()
                    )
                ).executeOrThrow()
            }
            when (apiResult) {

                is ResourceState.Success -> {
                    apiResult.successData?.data?.attributes?.let { response ->
                        preferenceManager.setStringData(
                            PREF_ACCESS_TOKEN,
                            response.accessToken ?: ""
                        )
                        preferenceManager.setStringData(
                            PREF_REFRESH_TOKEN,
                            response.refreshToken ?: ""
                        )
                        emit(ResourceState.Success(apiResult.successData.data))
                    } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> {
                    emit(ResourceState.Error(apiResult.error))
                }
                is ResourceState.GenericError -> {
                    emit(
                        ResourceState.GenericError(
                            apiResult.code,
                            apiResult.error
                        )
                    )
                }
                ResourceState.NetworkError -> {
                    emit(ResourceState.NetworkError)
                }

                ResourceState.Loading -> emit(ResourceState.Loading)
                else -> {emit(ResourceState.NetworkError)}
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message ?: UNKNOWN_ERROR_MESSAGE))
        }
}