package com.kks.nimbletest.data.network

import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.LoginResponse
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.data.network.reponse.UserResponse
import com.kks.nimbletest.data.network.request.LoginRequest
import com.kks.nimbletest.data.network.request.RefreshTokenRequest
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

interface ApiInterface {

    @POST("api/v1/oauth/token")
    fun loginUser(
        @Body loginRequest: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    @POST("api/v1/oauth/token")
    fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Call<BaseResponse<LoginResponse>>

    @GET("api/v1/surveys")
    fun getSurveyList(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") pageSize: Int,
    ): Call<BaseResponse<List<SurveyResponse>>?>

    @GET("api/v1/me")
    fun getUserDetail(): Call<BaseResponse<UserResponse>>

}