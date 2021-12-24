package com.kks.nimbletest.data.network.request

import com.google.gson.annotations.SerializedName

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

data class RefreshTokenRequest(
    @SerializedName("grant_type")
    val grantType: String = "refresh_token",
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)