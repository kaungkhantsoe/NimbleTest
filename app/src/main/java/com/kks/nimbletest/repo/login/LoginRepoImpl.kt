package com.kks.nimbletest.repo.login

import com.kks.nimbletest.BuildConfig
import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.constants.PrefConstants
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.data.network.request.LoginRequest
import com.kks.nimbletest.util.PreferenceManager
import com.kks.nimbletest.util.executeOrThrow
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
    private val preferenceManager: PreferenceManager
) : LoginRepo {
    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>> =
        flow {

            emit(ResourceState.Loading)

            when {
                email.isEmpty() -> emit(ResourceState.Error(AppConstants.error_email_empty))
                password.isEmpty() -> emit(ResourceState.Error(AppConstants.error_password_empty))
                else -> {
                    val apiResult = safeApiCall(Dispatchers.IO) {
                        apiInterface.loginUser(
                            LoginRequest(
                                email = email,
                                password = password,
                                client_id = BuildConfig.CLIENT_ID,
                                client_secret = BuildConfig.CLIENT_SECRET
                            )
                        )
                            .executeOrThrow()
                    }
                    when (apiResult) {
                        is ResourceState.Success -> {
                            apiResult.successData?.data?.let { loginResponse ->
                                preferenceManager.setStringData(
                                    PrefConstants.PREF_ACCESS_TOKEN,
                                    loginResponse.attributes?.access_token ?: ""
                                )
                                preferenceManager.setStringData(
                                    PrefConstants.PREF_REFRESH_TOKEN,
                                    loginResponse.attributes?.refresh_token ?: ""
                                )
                                emit(ResourceState.Success(loginResponse))

                            } ?: emit(ResourceState.Error(AppConstants.SUCCESS_WITH_NULL_ERROR))
                        }
                        ResourceState.EndReach -> emit(ResourceState.EndReach)
                        is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                        is ResourceState.GenericError -> emit(
                            ResourceState.GenericError(
                                apiResult.code,
                                apiResult.error
                            )
                        )
                        ResourceState.NetworkError -> emit(ResourceState.NetworkError)
                        else -> {
                            emit(ResourceState.NetworkError)
                        }
                    }
                }
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message ?: AppConstants.UNKNOWN_ERROR_MESSAGE))
        }
}