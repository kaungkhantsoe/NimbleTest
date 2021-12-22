package com.kks.nimbletest

import com.kks.nimbletest.util.CustomKeyProvider

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

class FakeCustomKeyGenerator: CustomKeyProvider {
    override fun getClientId(): String = "client_id"

    override fun getClientSecret(): String = "client_secret"
}