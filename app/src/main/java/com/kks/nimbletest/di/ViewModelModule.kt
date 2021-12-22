package com.kks.nimbletest.di

import com.kks.nimbletest.data.network.ApiInterface
import com.kks.nimbletest.repo.forget.ForgetPasswordRepo
import com.kks.nimbletest.repo.forget.ForgetPasswordRepoImpl
import com.kks.nimbletest.repo.home.HomeRepo
import com.kks.nimbletest.repo.home.HomeRepoImpl
import com.kks.nimbletest.repo.login.LoginRepo
import com.kks.nimbletest.repo.login.LoginRepoImpl
import com.kks.nimbletest.util.CustomKeyGenerator
import com.kks.nimbletest.util.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideLoginRepoImpl(
        apiInterface: ApiInterface,
        preferenceManager: PreferenceManager
    ): LoginRepo =
        LoginRepoImpl(apiInterface, preferenceManager, CustomKeyGenerator())

    @Provides
    fun provideHomeRepoImpl(
        apiInterface: ApiInterface
    ): HomeRepo =
        HomeRepoImpl(apiInterface)

    @Provides
    fun provideForgetPasswordRepoImpl(
        apiInterface: ApiInterface
    ): ForgetPasswordRepo =
        ForgetPasswordRepoImpl(apiInterface,CustomKeyGenerator())

}