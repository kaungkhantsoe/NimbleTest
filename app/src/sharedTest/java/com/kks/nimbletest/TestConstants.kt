package com.kks.nimbletest

import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.LoginAttributesResponse
import com.kks.nimbletest.data.network.reponse.LoginResponse

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

object TestConstants {
    val loginAttributesResponse = LoginAttributesResponse(
        "",
        "",
        1000,
        "",
        1000L
    )
    val loginResponse = LoginResponse(
        0,
        "",
        loginAttributesResponse
    )
    val baseLoginResponse =
        BaseResponse(
            loginResponse
        )
}