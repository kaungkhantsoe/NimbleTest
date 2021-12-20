package com.kks.nimbletest.util

import org.mockito.Mockito

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

/*
 If you have a method written in kotlin that does not take a nullable parameter,
 then we cannot match with it using Mockito.any(). This is because it can
 return void and this is not assignable to a non-nullable parameter.

 Reference: https://stackoverflow.com/questions/59230041/argumentmatchers-any-must-not-be-null
 */
object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T =  null as T
}