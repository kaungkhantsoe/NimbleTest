package com.kks.nimbletest.repo.forget

import com.kks.nimbletest.data.network.ResourceState
import kotlinx.coroutines.flow.Flow

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

interface ForgetPasswordRepo {
    fun sendForgetPasswordEmail(email: String): Flow<ResourceState<String>>
}