package com.kks.nimbletest

import com.kks.nimbletest.data.network.reponse.*

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

object TestConstants {

    // Login Success
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

    // Survey Success
    val surveyResponse = listOf(SurveyResponse(null,null,null,null))
    val metaResponse = MetaResponse(1,1,1,1,"")
    val baseSurveyResponse = BaseResponse(obj = surveyResponse, meta = metaResponse)


    // User Detail Success
    val userResponse = UserResponse("","",null)
    val baseUserResponse = BaseResponse(obj = userResponse)

    // Forget Password Success
    val forgetNull: String? = null
    val baseForgetPasswordResponse = BaseResponse(obj = forgetNull, meta = MetaResponse(1,1,1,1,"Success"))
}