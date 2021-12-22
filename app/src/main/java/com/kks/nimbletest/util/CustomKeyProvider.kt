package com.kks.nimbletest.util

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

interface CustomKeyProvider {
    fun getClientId(): String
    fun getClientSecret(): String
}