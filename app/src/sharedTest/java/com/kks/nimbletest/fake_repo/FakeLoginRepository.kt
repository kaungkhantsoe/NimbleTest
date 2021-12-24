package com.kks.nimbletest.fake_repo

import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.repo.login.LoginRepo
import com.kks.nimbletest.util.error_email_empty
import com.kks.nimbletest.util.error_password_empty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class FakeLoginRepository: LoginRepo {

    var clientId = ""
    var clientSecret = ""

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>> =
        flow {
            emit(ResourceState.Loading)

            if (clientId.isEmpty() || clientSecret.isEmpty())
                emit(ResourceState.GenericError(403,"invalid_client"))
            else if (email == "invalid" || password == "invalid")
                emit(ResourceState.GenericError(400,"invalid_grant"))
            else if (email.isEmpty())
                emit(ResourceState.Error(error_email_empty))
            else if (password.isEmpty())
                emit(ResourceState.Error(error_password_empty))
            else
                emit(ResourceState.Success(TestConstants.baseLoginResponse.data!!))
        }
}