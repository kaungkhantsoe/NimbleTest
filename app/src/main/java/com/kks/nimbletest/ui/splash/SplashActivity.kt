package com.kks.nimbletest.ui.splash

import android.app.ActivityOptions
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import com.kks.nimbletest.R
import com.kks.nimbletest.databinding.ActivitySplashBinding
import com.kks.nimbletest.ui.base.BaseViewBindingActivity
import com.kks.nimbletest.ui.home.HomeActivity
import com.kks.nimbletest.ui.login.LoginActivity
import com.kks.nimbletest.util.PREF_LOGGED_IN
import com.kks.nimbletest.util.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 18/12/2021
 */

@AndroidEntryPoint
class SplashActivity : BaseViewBindingActivity<ActivitySplashBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() = ActivitySplashBinding::inflate

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun setup() {

        binding.ivLogo.animate()
            .alpha(1.0f)
            .duration = 1000

        object : CountDownTimer(1500, 100) {
            override fun onTick(second: Long) {

            }

            override fun onFinish() {
                if (preferenceManager.getBooleanData(PREF_LOGGED_IN)) {
                    startActivity(Intent(this@SplashActivity,HomeActivity::class.java))
                } else {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val options = ActivityOptions
                        .makeSceneTransitionAnimation(
                            this@SplashActivity,
                            binding.ivLogo,
                            getString(R.string.transition_name_logo)
                        )
                    startActivity(intent, options.toBundle())
                }
                finish()
            }
        }.start()
    }

}

















