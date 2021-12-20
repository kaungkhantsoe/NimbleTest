package com.kks.nimbletest.data.network.request

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

data class LoginRequest(
    val grant_type: String = "password",
    val email: String,
    val password: String,
    val client_id: String,
    val client_secret: String
)