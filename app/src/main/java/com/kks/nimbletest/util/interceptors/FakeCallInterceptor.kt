package com.kks.nimbletest.util.interceptors

import com.kks.nimbletest.BuildConfig
import com.kks.nimbletest.util.MockResponseFileReader
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class FakeCallInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri()
            val query = uri.query
            val parsedQuery = query.split("&").associate {
                val (left, right) = it.split("=")
                left to right
            }
            val responseString = when {
                uri.toString().endsWith("token") -> {
                    if (parsedQuery["client_id"].isNullOrEmpty() || parsedQuery["client_secret"].isNullOrEmpty()) {
                        MockResponseFileReader("login_invalid_client.json").content
                    }
                    else if (parsedQuery["email"].isNullOrEmpty() || parsedQuery["password"].isNullOrEmpty()) {
                        MockResponseFileReader("login_invalid_grant.json").content
                    }
                    else {
                        MockResponseFileReader("login_success.json").content
                    }
                }
                else -> ""
            }

            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    responseString.toByteArray().toResponseBody("application/json".toMediaTypeOrNull())
                )
                .header("content-type", "application/json")
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError("MockInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
    }
}