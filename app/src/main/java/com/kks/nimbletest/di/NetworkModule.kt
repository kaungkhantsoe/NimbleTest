package com.kks.nimbletest.di

import android.content.Context
import com.kks.nimbletest.BuildConfig
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.repo.token.TokenRepoImpl
import com.kks.nimbletest.util.PreferenceManager
import com.kks.nimbletest.util.interceptors.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providePreferenceManager(@ApplicationContext appContext: Context): PreferenceManager =
        PreferenceManager(appContext)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        /*accessTokenInterceptor: AccessTokenInterceptor,*/
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .authenticator(TokenAuthenticator()) /* Refresh token interceptor */
            .addInterceptor(loggingInterceptor)
            /*.addInterceptor(accessTokenInterceptor)*/
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }


    @Provides
    fun provideTokenRepoImpl(
        apiInterface: ApiInterface,
        preferenceManager: PreferenceManager
    ): TokenRepoImpl =
        TokenRepoImpl(apiInterface, preferenceManager)

}