package com.kks.nimbletest.util

import com.beust.klaxon.Klaxon
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStreamReader

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class MockResponseFileReader(path: String) {
    val content: String
    val contentAsJsonObj: JsonObject
        get() {
            return JsonParser().parse(content).asJsonObject
        }

    inline fun <reified T: Any> getAs(): T? {
        return Klaxon().parse<T>(content)
    }

    init {
        val reader = InputStreamReader(this.javaClass.classLoader.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }

}