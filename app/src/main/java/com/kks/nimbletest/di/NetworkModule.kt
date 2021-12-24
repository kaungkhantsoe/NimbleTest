package com.kks.nimbletest.di

import android.content.Context
import com.kks.nimbletest.BuildConfig
import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.repo.token.TokenRepo
import com.kks.nimbletest.repo.token.TokenRepoImpl
import com.kks.nimbletest.util.CustomKeyGenerator
import com.kks.nimbletest.util.PreferenceManager
import com.kks.nimbletest.util.interceptors.CustomAccessTokenInterceptor
import com.kks.nimbletest.util.interceptors.TokenAuthenticator
import dagger.Lazy
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
import javax.inject.Singleton

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext appContext: Context): PreferenceManager =
        PreferenceManager(appContext)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        preferenceManager: PreferenceManager,
        tokenRepo: Lazy<TokenRepoImpl>,
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(CustomAccessTokenInterceptor(preferenceManager,tokenRepo))
            .authenticator(TokenAuthenticator(preferenceManager,tokenRepo)) /* Refresh token interceptor */
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Singleton
    @Provides
    fun provideTokenRepoImpl(
        apiInterface: ApiInterface,
        preferenceManager: PreferenceManager
    ): TokenRepoImpl =
        TokenRepoImpl(apiInterface, preferenceManager, CustomKeyGenerator())

}