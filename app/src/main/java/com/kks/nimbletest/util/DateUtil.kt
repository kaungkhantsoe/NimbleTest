package com.kks.nimbletest.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

const val EEEE_comma_MMMMM_d_format = "EEEE,MMMM d"

object DateUtil {

    private val EEEE_comma_MMMMM_d =
        SimpleDateFormat(EEEE_comma_MMMMM_d_format, Locale.ENGLISH)

    fun getBeautifiedCurrentDate() = EEEE_comma_MMMMM_d.format(Date())
}