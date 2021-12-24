package com.kks.nimbletest.util

import com.kks.nimbletest.data.network.reponse.BaseResponse
import com.kks.nimbletest.data.network.reponse.LoginResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.net.UnknownHostException

const val UNKNOWN_ERROR = "Unknown error"
const val SUCCESS_WITH_NULL_ERROR = "Success with null error"
const val UNKNOWN_ERROR_MESSAGE = "Unknown error message"
const val NETWORK_ERROR = "Network Error"

const val error_email_empty = "Email cannot be empty"
const val error_password_empty = "Password cannot be empty"
const val success = "success"

fun <T> Call<T>.executeOrThrow(): T? {
    var response: Response<T>? = null
    try {
        response = this.execute()
        if (response.isSuccessful.not()) {
            response.errorBody()?.let {
                val errCode = response.code()
                if (errCode in 400..499)
                    throw HttpException(
                        Response.error<BaseResponse<T>>(
                            errCode,
                            it
                        )
                    )
                else {

                    val jsonObject = JSONObject(it.string())

                    // If errorBody has "errors"
                    if (jsonObject.has("errors")) {
                        // Get error array from errorBody
                        val jsonArray = jsonObject.getJSONArray("errors")
                        // If errors array is not empty and first object of error array has "code" in it
                        if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("code")) {
                            // Get error code
                            val code = jsonArray.getJSONObject(0).getString("code")
                            // Throw exception with error code
                            throw Exception(code)
                        }
                    }

                    throw Exception(UNKNOWN_ERROR)
                }
            }

        }
    } catch (httpException: HttpException) {
        response?.errorBody()?.let {
            throw HttpException(
                Response.error<BaseResponse<LoginResponse>>(
                    httpException.code(),
                    it
                )
            )
        }
        throw httpException
    } catch (unknownHostException: UnknownHostException) {
        throw unknownHostException
    } catch (e: Exception) {
        throw e
    }
    return response.body()

}