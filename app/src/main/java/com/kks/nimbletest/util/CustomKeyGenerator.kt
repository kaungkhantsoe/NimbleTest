package com.kks.nimbletest.util

class CustomKeyGenerator: CustomKeyProvider {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    private external fun decryptId(): String

    private external fun decryptSecret(): String

    private external fun decryptIVKey(): String

    private external fun decryptSecreteKey(): String

    private external fun decryptAlgorithm(): String

    private external fun decryptTransformation(): String

    override fun getClientId(): String = decryptId()

    override fun getClientSecret(): String = decryptSecret()

}