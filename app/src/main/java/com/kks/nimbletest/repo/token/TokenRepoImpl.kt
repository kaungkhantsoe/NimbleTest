package com.kks.nimbletest.repo.token

import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.constants.PrefConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.data.network.request.RefreshTokenRequest
import com.kks.nimbletest.util.CustomKeyProvider
import com.kks.nimbletest.util.PreferenceManager
import com.kks.nimbletest.util.executeOrThrow
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
                        refresh_token = refreshToken,
                        client_id = customKeyProvider.getClientId(),
                        client_secret = customKeyProvider.getClientSecret()
                    )
                ).executeOrThrow()
            }
            when (apiResult) {

                is ResourceState.Success -> {
                    apiResult.successData?.data?.attributes?.let { response ->
                        preferenceManager.setStringData(
                            PrefConstants.PREF_ACCESS_TOKEN,
                            response.access_token ?: ""
                        )
                        preferenceManager.setStringData(
                            PrefConstants.PREF_REFRESH_TOKEN,
                            response.refresh_token ?: ""
                        )
                        emit(ResourceState.Success(apiResult.successData.data))
                    } ?: emit(ResourceState.Error(AppConstants.SUCCESS_WITH_NULL_ERROR))
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
            emit(ResourceState.Error(error.message ?: AppConstants.UNKNOWN_ERROR_MESSAGE))
        }
}