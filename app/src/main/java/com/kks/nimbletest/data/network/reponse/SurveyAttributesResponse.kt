package com.kks.nimbletest.data.network.reponse

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

data class SurveyAttributesResponse(
    val title: String?,
    val description: String?,
    val thank_email_above_threshold: String?,
    val thank_email_below_threshold: String?,
    val is_active: Boolean?,
    val cover_image_url: String?,
    val created_at: String?,
    val active_at: String?,
    val survey_type: String?
)