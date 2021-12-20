package com.kks.nimbletest.repo.login

import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

interface LoginRepo {
    fun loginWithEmailAndPassword(email: String, password: String): Flow<ResourceState<LoginResponse>>
}