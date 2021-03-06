package com.kks.nimbletest.fake_repo

import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.repo.forget.ForgetPasswordRepo
import com.kks.nimbletest.util.error_email_empty
import com.kks.nimbletest.util.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

class FakeForgetPasswordRepository: ForgetPasswordRepo {

    var clientId = ""
    var clientSecret = ""

    override fun sendForgetPasswordEmail(email: String): Flow<ResourceState<String>> =
        flow {
            emit(ResourceState.Loading)

            if (clientId.isEmpty() || clientSecret.isEmpty())
                emit(ResourceState.GenericError(403,"invalid_client"))
            else if (email == "invalid")
                emit(ResourceState.GenericError(403,"invalid_client"))
            else if (email.isEmpty())
                emit(ResourceState.Error(error_email_empty))
            else
                emit(ResourceState.Success(success))
        }
}