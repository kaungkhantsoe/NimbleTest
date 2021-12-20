package com.kks.nimbletest.fake_repo

import com.kks.nimbletest.TestConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.repo.login.LoginRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class FakeLoginRepository: LoginRepo {

    var client_id = ""
    var client_secret = ""

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResourceState<LoginResponse>> =
        flow {
            emit(ResourceState.Loading)

            if (client_id.isEmpty() || client_secret.isEmpty())
                emit(ResourceState.GenericError(403,"invalid_client"))
            else if (email.isEmpty() || password.isEmpty())
                emit(ResourceState.GenericError(400,"invalid_grant"))
            else
                emit(ResourceState.Success(TestConstants.baseLoginResponse.data!!))
        }
}