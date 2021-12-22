package com.kks.nimbletest.fake_repo

import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.repo.forget.ForgetPasswordRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

class FakeForgetPasswordRepository: ForgetPasswordRepo {

    var client_id = ""
    var client_secret = ""

    override fun sendForgetPasswordEmail(email: String): Flow<ResourceState<String>> =
        flow {
            emit(ResourceState.Loading)

            if (client_id.isEmpty() || client_secret.isEmpty())
                emit(ResourceState.GenericError(403,"invalid_client"))
            else if (email.isEmpty())
                emit(ResourceState.Error(AppConstants.error_email_empty))
            else
                emit(ResourceState.Success(AppConstants.success))
        }
}