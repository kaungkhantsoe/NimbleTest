package com.kks.nimbletest.util.interceptors

import android.content.Context
import com.kks.nimbletest.constants.PrefConstants
import com.kks.nimbletest.repo.token.TokenRepo
import com.kks.nimbletest.repo.token.TokenRepoImpl
import com.kks.nimbletest.util.PreferenceManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

class CustomAccessTokenInterceptor(private val context: Context) : Interceptor {

    lateinit var preferenceManager: PreferenceManager

    lateinit var tokenRepo: TokenRepoImpl

    private var isRefreshed = false

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TokenAuthenticatorEntryPoint {
        fun preferenceManager(): PreferenceManager
        fun tokenRepo(): TokenRepoImpl
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            TokenAuthenticator.TokenAuthenticatorEntryPoint::class.java
        )

        preferenceManager = entryPoint.preferenceManager()
        tokenRepo = entryPoint.tokenRepo()

        val accessToken: String =
            preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN) ?: ""
        val request: Request = newRequestWithAccessToken(chain.request(), accessToken)

        val response: Response = chain.proceed(request)

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val job = CoroutineScope(Dispatchers.IO).launch {
                tokenRepo.refreshToken(
                    preferenceManager.getStringData(PrefConstants.PREF_REFRESH_TOKEN) ?: ""
                ).collectLatest {
                    isRefreshed = true
                }
            }

            synchronized(this) {
                while (!isRefreshed) {
                    // Wait for cache
                }
                isRefreshed = false
                val newAccessToken: String =
                    preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN) ?: ""
                job.cancel()
                // Access token is refreshed in another thread.
                if (accessToken != newAccessToken) {
                    return chain.proceed(newRequestWithAccessToken(request, newAccessToken))
                }
            }
        }
        return response
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request =
        // Create new request with new accessToken fetched earlier
        request.newBuilder().header(
            "Authorization",
            "Bearer $accessToken"
        ).build()


}