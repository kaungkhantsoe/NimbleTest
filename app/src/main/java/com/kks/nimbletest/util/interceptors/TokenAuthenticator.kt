package com.kks.nimbletest.util.interceptors

import com.kks.nimbletest.constants.PrefConstants
import com.kks.nimbletest.repo.token.TokenRepo
import com.kks.nimbletest.util.PreferenceManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

/*
Reference: http://sangsoonam.github.io/2019/03/06/okhttp-how-to-refresh-access-token-efficiently.html
 */
class TokenAuthenticator : Authenticator {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var tokenRepo: TokenRepo

    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            /*
            Check if request has header with accessToken
            Note:: AccessToken is already stored inside the preference when login
             */
            if (!isRequestWithAccessToken(response) || preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN) == null)
                return null

            synchronized(this) {

                // Request new token with refreshToken
                tokenRepo.refreshToken(
                    preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN) ?: ""
                )

                // Create new request with new accessToken fetched earlier
                return response.request.newBuilder().header(
                    "Authorization",
                    "Bearer ${preferenceManager.getStringData(PrefConstants.PREF_ACCESS_TOKEN)}"
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