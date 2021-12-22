package com.kks.nimbletest.data.network

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

sealed class ResourceState<out T> {
    object Loading: ResourceState<Nothing>()
    data class Success<out T>(val successData: T) : ResourceState<T>()
    object EndReach : ResourceState<Nothing>()

    class Error(val error: String) : ResourceState<Nothing>()
    data class GenericError(val code: Int? = null, val error: String? = null) :
        ResourceState<Nothing>()
    object NetworkError : ResourceState<Nothing>()
    object ProtocolError : ResourceState<Nothing>()

}