package com.kks.nimbletest.util

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

class FadeInOutPageTransformer: ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        if (position <= -1.0f || position >= 1.0f) {
            view.translationX = view.width * position
            view.alpha = 0.0f
        } else if (position == 0.0f) {
            view.translationX = view.width * position
            view.alpha = 1.0f
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.translationX = view.width * -position
            view.alpha = 1.0f - Math.abs(position)
        }
    }
}