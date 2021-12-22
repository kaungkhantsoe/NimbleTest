package com.kks.nimbletest.data.network.request

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

data class ForgetPasswordRequest(
    val user: ForgetPasswordUserRequest,
    val client_id: String,
    val client_secret: String
)