package com.kks.nimbletest.data.network.reponse

import com.google.gson.annotations.SerializedName

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

data class SurveyAttributesResponse(
    val title: String?,
    val description: String?,
    @SerializedName("thank_email_above_threshold")
    val thankEmailAboveThreshold: String?,
    @SerializedName("thank_email_below_threshold")
    val thankEmailBelowThreshold: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("cover_image_url")
    val coverImageUrl: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("active_at")
    val activeAt: String?,
    @SerializedName("survey_type")
    val surveyType: String?
)