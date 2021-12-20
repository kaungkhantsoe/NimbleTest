package com.kks.nimbletest.data.network.reponse

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

data class LoginAttributesResponse(
    val access_token: String?,
    val token_type: String?,
    val expires_in: Int?,
    val refresh_token: String?,
    val created_at: Long?
)