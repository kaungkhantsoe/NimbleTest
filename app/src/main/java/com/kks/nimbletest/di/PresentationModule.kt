package com.kks.nimbletest.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.kks.nimbletest.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .centerCrop()
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
    }
}