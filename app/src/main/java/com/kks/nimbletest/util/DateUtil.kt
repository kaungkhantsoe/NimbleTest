package com.kks.nimbletest.util

import com.kks.nimbletest.constants.DateConstants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kaungkhantsoe at 21/12/2021
 */

object DateUtil {

    private val EEEE_comma_MMMMM_d =
        SimpleDateFormat(DateConstants.EEEE_comma_MMMMM_d, Locale.ENGLISH)

    fun getBeautifiedCurrentDate() = EEEE_comma_MMMMM_d.format(Date())
}