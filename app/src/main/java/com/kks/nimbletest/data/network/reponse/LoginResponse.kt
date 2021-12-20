package com.kks.nimbletest.data.network.reponse

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

data class LoginResponse(
    val id: Int?,
    val type: String?,
    val attributes: LoginAttributesResponse?
)