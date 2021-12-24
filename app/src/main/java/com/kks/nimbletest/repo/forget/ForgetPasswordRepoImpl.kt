package com.kks.nimbletest.repo.forget

import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.request.ForgetPasswordRequest
import com.kks.nimbletest.data.network.request.ForgetPasswordUserRequest
import com.kks.nimbletest.util.*
import com.kks.nimbletest.util.extensions.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

class ForgetPasswordRepoImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val customKeyProvider: CustomKeyProvider
) : ForgetPasswordRepo {

    override fun sendForgetPasswordEmail(email: String): Flow<ResourceState<String>> =
        flow {
            val apiResult = safeApiCall(Dispatchers.IO) {

                apiInterface.sendForgetPasswordMail(
                    ForgetPasswordRequest(
                        ForgetPasswordUserRequest(email),
                        customKeyProvider.getClientId(),
                        customKeyProvider.getClientSecret()
                    )
                ).executeOrThrow()
            }

            when (apiResult) {
                ResourceState.Loading -> emit(ResourceState.Loading)

                is ResourceState.Success -> {
                    apiResult.successData?.meta?.message?.let {
                        emit(ResourceState.Success(it))
                    } ?: emit(ResourceState.Success(success))
                }

                ResourceState.ProtocolError -> emit(ResourceState.ProtocolError)
                ResourceState.NetworkError -> emit(ResourceState.NetworkError)
                is ResourceState.Error -> emit(ResourceState.Error(apiResult.error))
                is ResourceState.GenericError -> emit(
                    ResourceState.GenericError(
                        apiResult.code,
                        apiResult.error
                    )
                )
                else -> {
                    // do nothing
                }
            }
        }.catch { error ->
            emit(ResourceState.Error(error.message ?: UNKNOWN_ERROR_MESSAGE))
        }
}