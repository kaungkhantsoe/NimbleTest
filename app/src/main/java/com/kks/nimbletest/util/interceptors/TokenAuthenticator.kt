package com.kks.nimbletest.util.interceptors

import com.kks.nimbletest.repo.token.TokenRepoImpl
import com.kks.nimbletest.util.PREF_ACCESS_TOKEN
import com.kks.nimbletest.util.PREF_REFRESH_TOKEN
import com.kks.nimbletest.util.PreferenceManager
import dagger.Lazy
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
class TokenAuthenticator(
    private val preferenceManager: PreferenceManager,
    private val tokenRepo: Lazy<TokenRepoImpl>,
    ) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        var isRefreshed = false

        try {
            /*
            Check if request has header with accessToken
            Note:: AccessToken is already stored inside the preference when login
             */
            if (!isRequestWithAccessToken(response) || preferenceManager.getStringData(PREF_ACCESS_TOKEN) == null)
                return null

            // Request new token with refreshToken
            val job = CoroutineScope(Dispatchers.IO).launch {
                tokenRepo.get().refreshToken(
                    preferenceManager.getStringData(PREF_REFRESH_TOKEN) ?: ""
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
                val newToken = preferenceManager.getStringData(PREF_ACCESS_TOKEN)
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
    }

    private fun isRequestWithAccessToken(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer")
    }
}