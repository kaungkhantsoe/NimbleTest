package com.kks.nimbletest.data.network.reponse

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

data class SurveyResponse(
    val id: String?,
    val type: String?,
    val attributes: SurveyAttributesResponse?,
    val relationships: SurveyRelationshipsResponse?
)