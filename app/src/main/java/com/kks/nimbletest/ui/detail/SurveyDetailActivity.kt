package com.kks.nimbletest.ui.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import com.kks.nimbletest.R
import com.kks.nimbletest.databinding.ActivitySurveyDetailBinding
import com.kks.nimbletest.ui.base.BaseViewBindingActivity

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

class SurveyDetailActivity: BaseViewBindingActivity<ActivitySurveyDetailBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context,SurveyDetailActivity::class.java))
        }
    }
    override val bindingInflater: (LayoutInflater) -> ActivitySurveyDetailBinding
        get() = ActivitySurveyDetailBinding::inflate

    override fun setup() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar,true)
    }
}