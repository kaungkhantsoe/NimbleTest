package com.kks.nimbletest.repo.token

import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.LoginResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

interface TokenRepo {
    fun refreshToken(refreshToken: String): Flow<ResourceState<LoginResponse>>
}