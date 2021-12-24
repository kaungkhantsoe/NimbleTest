package com.kks.nimbletest.data.network.reponse

import com.google.gson.annotations.SerializedName

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

data class UserAttributesResponse(
    val email: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)