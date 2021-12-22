package com.kks.nimbletest.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.bumptech.glide.RequestManager
import com.kks.nimbletest.R
import com.kks.nimbletest.adapter.SurveyAdapter
import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.databinding.ActivityHomeBinding
import com.kks.nimbletest.ui.base.BaseViewBindingActivity
import com.kks.nimbletest.ui.detail.SurveyDetailActivity
import com.kks.nimbletest.ui.login.LoginActivity
import com.kks.nimbletest.util.DateUtil
import com.kks.nimbletest.util.FadeInOutPageTransformer
import com.kks.nimbletest.util.PreferenceManager
import com.kks.nimbletest.util.extensions.toast
import com.kks.nimbletest.util.listeners.RecyclerViewItemClickListener
import com.kks.nimbletest.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

@AndroidEntryPoint
class HomeActivity : BaseViewBindingActivity<ActivityHomeBinding>(), RecyclerViewItemClickListener {

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var preferenceManager: PreferenceManager

    lateinit var viewModel: HomeViewModel

    private lateinit var adapter: SurveyAdapter

    private var endOfList = false

    private val surveyList = mutableListOf<SurveyResponse>()

    private val pageSize = 2
    private var pageNumber = 1

    private var isLoading = false

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        private var mPosition = 0

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == SCROLL_STATE_IDLE) {
                if (!isLoading && mPosition == surveyList.size-1 && !endOfList) {
                    pageNumber++
                    getSurveyList(pageNumber)
                }
                updateTexts(mPosition)
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            mPosition = position
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    override fun setup() {

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.ibAction.setOnClickListener {
            SurveyDetailActivity.start(this@HomeActivity)
        }

        binding.tvDate.text = DateUtil.getBeautifiedCurrentDate()

        adapter = SurveyAdapter(this,requestManager)
        binding.vpSurveys.adapter = adapter
        binding.vpSurveys.setPageTransformer(FadeInOutPageTransformer())
        binding.vpSurveys.registerOnPageChangeCallback(onPageChangeCallback)

        observeSurveyList()
        getSurveyList(1)

        observeUserDetail()
        viewModel.getUserDetail()
    }

    private fun updateTexts(position: Int) {
        if (surveyList.size > position) {
            val survey = surveyList[position]
            binding.tvTitle.text = survey.attributes?.title
            binding.tvDescription.text = survey.attributes?.description
        }
    }

    private fun getSurveyList(page: Int) {
        if (page == 1)
            surveyList.clear()
        if (!endOfList) {
            viewModel.getSurveyList(page, pageSize)
        }
    }

    private fun observeSurveyList() {
        viewModel.surveyListLiveData.observe(this) {
            when (it) {
                is ResourceState.Success -> {

                    if (pageNumber == 1) {
                        surveyList.clear()
                        adapter.clearData()
                    }

                    showLoading(false)

                    surveyList.addAll(it.successData)

                    adapter.addAll(it.successData)

                    binding.dotsIndicator.setViewPager2(binding.vpSurveys)

                    if(pageNumber == 1 && surveyList.size > 0)
                        updateTexts(0)
                }
                is ResourceState.Error -> handleError(it.error)
                is ResourceState.GenericError -> {
                    if (pageNumber > 1 && it.code == 404)
                        endOfList = true
                    else
                        handleError(it.error)
                }
                ResourceState.NetworkError -> handleError(AppConstants.NETWORK_ERROR)
                ResourceState.Loading -> {
                    showLoading()
                }
                ResourceState.EndReach -> {
                    endOfList = true
                }
                ResourceState.ProtocolError -> {
                    showSessionExpire()
                }
            }
        }
    }

    private fun showSessionExpire() {
        //Too many follow-up requests error was thrown
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.txt_session_is_expired))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok
            ) { dialog, id ->
                preferenceManager.deleteAllData()
                startActivity(Intent(this@HomeActivity,LoginActivity::class.java))
                finish()
            }
            .show()
    }

    private fun observeUserDetail() {
        viewModel.userLiveData.observe(this) {
            when(it) {
                is ResourceState.Success -> {
                    requestManager
                        .load(it.successData.attributes?.avatar_url)
                        .into(binding.ivUser)
                }
                is ResourceState.Error -> handleError("Profile: ${it.error}")
                is ResourceState.GenericError -> handleError("Profile: ${it.error}")
                ResourceState.NetworkError -> handleError("Profile: ${AppConstants.NETWORK_ERROR}")
                else -> {}
            }
        }
    }

    private fun showLoading(show: Boolean = true) {
        isLoading = show
        if (show && pageNumber == 1) {
            binding.shimmerView.visibility = View.VISIBLE
            binding.clContent.visibility = View.GONE
            binding.shimmerView.startShimmer()
        } else {
            binding.shimmerView.visibility = View.GONE
            binding.clContent.visibility = View.VISIBLE
            binding.shimmerView.stopShimmer()
        }
    }

    private fun handleError(err: String?) {
        toast(err ?: "")
        pageNumber--
        showLoading(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vpSurveys.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onItemClick(position: Int) {
        SurveyDetailActivity.start(this@HomeActivity)
    }
}