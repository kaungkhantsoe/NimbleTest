package com.kks.nimbletest.data.network.reponse

/**
 * Created by kaungkhantsoe at 19/12/2021
 */

class BaseResponse<D> constructor(obj: D, errors: List<ErrorResponse>? = null, meta: MetaResponse? = null) {
    val data: D? = obj
    val errors: List<ErrorResponse>? = errors
    val meta: MetaResponse? = null
}