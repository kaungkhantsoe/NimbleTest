package com.kks.nimbletest

import android.app.Application
import com.kks.nimbletest.util.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by kaungkhantsoe at 18/12/2021
 */
@HiltAndroidApp
class NimbleApp: Application() {

    override fun onCreate() {
        super.onCreate()
        plantTimber()
    }

    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "Class:%s: Line: %s, Method: %s",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}