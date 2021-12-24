package com.kks.nimbletest.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.kks.nimbletest.R

const val PREF_ACCESS_TOKEN = "access_token"
const val PREF_REFRESH_TOKEN = "refresh_token"
const val PREF_LOGGED_IN = "logged_in"

class PreferenceManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("pref_${context.getString(R.string.app_name)}", Context.MODE_PRIVATE)

    fun setStringData(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun setIntegerData(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun setLongData(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun setBooleanData(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun <T> setList(key: String?, list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        preferences.edit().putString(key, json).apply()
    }


    fun getStringData(key: String): String? {
        return preferences.getString(key, "")
    }

    fun getIntegerData(key: String): Int {
        return preferences.getInt(key, -1)
    }

    fun getLongData(key: String): Long {
        return preferences.getLong(key, -1L)
    }

    fun getBooleanData(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun deleteAllData() {
        return preferences.edit().clear().apply()
    }
}