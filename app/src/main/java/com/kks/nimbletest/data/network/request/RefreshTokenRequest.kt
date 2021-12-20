package com.kks.nimbletest.data.network.request

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

data class RefreshTokenRequest(
    val grant_type: String = "refresh_token",
    val refresh_token: String,
    val client_id: String,
    val client_secret: String
)