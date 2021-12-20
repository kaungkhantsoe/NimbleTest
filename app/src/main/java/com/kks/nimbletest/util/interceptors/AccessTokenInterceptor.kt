package com.kks.nimbletest.util.interceptors

import com.kks.nimbletest.constants.PrefConstants
import com.kks.nimbletest.data.network.ApiInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

//class AccessTokenInterceptor(
//    private val preferenceManager: PreferenceManager,
//    private val apiInterface: ApiInterface
//) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//
//        //////
//        val accessToken: String =
//            preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN) ?: ""
//        val request: Request = newRequestWithAccessToken(chain.request(), accessToken)
//        val response = chain.proceed(request)
//
//        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
//            synchronized(this) {
//                val newAccessToken: String = accessTokenRepository.getAccessToken()
//                // Access token is refreshed in another thread.
//                if (accessToken != newAccessToken) {
//                    return chain.proceed(newRequestWithAccessToken(request, newAccessToken))
//                }
//
//                // Need to refresh an access token
//                val updatedAccessToken: String = accessTokenRepository.refreshAccessToken()
//                // Retry the request
//                return chain.proceed(newRequestWithAccessToken(request, updatedAccessToken))
//            }
//        }
//    }
//
//    fun newRequestWithAccessToken(request: Request, accessToken: String): Request {
//        val requestBuilder = request.newBuilder()
//
//        preferenceManager.getStringData(PrefConstants.PREF_USER_TOKEN).let {
//            it?.let {
//                requestBuilder.addHeader("Authorization", "Bearer $it")
//            }
//        }
//
//        return requestBuilder.build()
//    }
//}