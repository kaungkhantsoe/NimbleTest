package com.kks.nimbletest.repo.login

import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.data.network.request.LoginRequest
import com.kks.nimbletest.util.*
import com.kks.nimbletest.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class LoginRepoImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val preferenceManager: PreferenceManager,
    private val customKeyProvider: CustomKeyProvider
) : LoginRepo {

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>> =
        flow {
            val apiResult = safeApiCall(Dispatchers.IO) {
                apiInterface.loginUser(
                    LoginRequest(
                        email = email,
                        password = password,
                        clientId = customKeyProvider.getClientId(),
                        clientSecret = customKeyProvider.getClientSecret()
                    )
                )
                    .executeOrThrow()
            }

            when (apiResult) {
                is ResourceState.Success -> {
                    apiResult.successData?.data?.let { loginResponse ->
                        preferenceManager.setStringData(
                            PREF_ACCESS_TOKEN,
                            loginResponse.attributes?.accessToken ?: ""
                        )
                        preferenceManager.setStringData(
                            PREF_REFRESH_TOKEN,
                            loginResponse.attributes?.refreshToken ?: ""
                        )
                        emit(ResourceState.Success(loginResponse))
                    } ?: emit(ResourceState.Error(SUCCESS_WITH_NULL_ERROR))
                }
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                is ResourceState.GenericError -> emit(
                    ResourceState.GenericError(
                        apiResult.code,
                        apiResult.error
                    )
                )
                else -> {
                    emit(ResourceState.NetworkError)
                }
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message ?: UNKNOWN_ERROR_MESSAGE))
        }
}
