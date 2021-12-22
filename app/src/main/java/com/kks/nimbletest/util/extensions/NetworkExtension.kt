package com.kks.nimbletest.util.extensions

import com.kks.nimbletest.data.network.ResourceState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ProtocolException
import java.net.UnknownHostException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResourceState<T> {
    return withContext(dispatcher) {
        try {
            withTimeout(7000) {
                ResourceState.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is ProtocolException -> ResourceState.ProtocolError
                is IOException -> ResourceState.NetworkError
                is UnknownHostException -> ResourceState.NetworkError
                is TimeoutCancellationException -> ResourceState.NetworkError
                is HttpException -> {
                    val code = throwable.code()
//                    val errorResponse = convertErrorBody(throwable)
                    val errorMsg = convertErrorBody(throwable)
                    ResourceState.GenericError(
                        code,
                        errorMsg
                    )
                }
                else -> {
                    ResourceState.Error(throwable.message ?: "Unknown error")
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    try {
        throwable.response()?.errorBody()?.let {
            val jsonObject = JSONObject(it.string())

            // If errorBody has "errors"
            if (jsonObject.has("errors")) {
                // Get error array from errorBody
                val jsonArray = jsonObject.getJSONArray("errors")
                // If errors array is not empty and first object of error array has "code" in it
                if (jsonArray.length() > 0 && jsonArray.getJSONObject(0).has("code")) {
                    // Get error code
                    val code = jsonArray.getJSONObject(0).getString("code")
                    // Throw exception with error code
                    return code
                }
            }
        }
    } catch (exception: Exception) {
        return null
    }
    return null

}
