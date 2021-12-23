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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.lang.Exception

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

/*
Reference: http://sangsoonam.github.io/2019/03/06/okhttp-how-to-refresh-access-token-efficiently.html
 */
class TokenAuthenticator(private val context: Context) : Authenticator {

    lateinit var preferenceManager: PreferenceManager

    lateinit var tokenRepo: TokenRepoImpl

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TokenAuthenticatorEntryPoint {
        fun preferenceManager(): PreferenceManager
        fun tokenRepo(): TokenRepoImpl
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val entryPoint =
            EntryPointAccessors.fromApplication(context, TokenAuthenticatorEntryPoint::class.java)

        preferenceManager = entryPoint.preferenceManager()
        tokenRepo = entryPoint.tokenRepo()
        var isRefreshed: Boolean = false

        try {
            /*
            Check if request has header with accessToken
            Note:: AccessToken is already stored inside the preference when login
             */
            if (!isRequestWithAccessToken(response) || preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN) == null)
                return null

            // Request new token with refreshToken
            val job = CoroutineScope(Dispatchers.IO).launch {
                tokenRepo.refreshToken(
                    preferenceManager.getStringData(PrefConstants.PREF_REFRESH_TOKEN) ?: ""
                ).collectLatest {
                    isRefreshed = true
                }
            }

            synchronized(this) {
                while (!isRefreshed) {
                    // wait for cache
                }
                isRefreshed = false
                // Create new request with new accessToken fetched earlier
                val newToken = preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN)
                job.cancel()
                return response.request.newBuilder().header(
                    "Authorization",
                    "Bearer $newToken"
                ).build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }

    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer")
    }
}