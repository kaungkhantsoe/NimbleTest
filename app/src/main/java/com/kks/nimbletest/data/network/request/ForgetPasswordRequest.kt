package com.kks.nimbletest.data.network.request

import com.google.gson.annotations.SerializedName

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

data class ForgetPasswordRequest(
    val user: ForgetPasswordUserRequest,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
)