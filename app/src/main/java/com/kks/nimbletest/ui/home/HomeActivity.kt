package com.kks.nimbletest.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.kks.kconnectioncheck.KConnectionCheck
import com.kks.nimbletest.R
import com.kks.nimbletest.adapter.SurveyAdapter
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.data.network.reponse.SurveyResponse
import com.kks.nimbletest.databinding.ActivityHomeBinding
import com.kks.nimbletest.ui.base.BaseViewBindingActivity
import com.kks.nimbletest.ui.detail.SurveyDetailActivity
import com.kks.nimbletest.ui.login.LoginActivity
import com.kks.nimbletest.util.*
import com.kks.nimbletest.util.extensions.loadImage
import com.kks.nimbletest.util.listeners.RecyclerViewItemClickListener
import com.kks.nimbletest.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

@AndroidEntryPoint
class HomeActivity :
    BaseViewBindingActivity<ActivityHomeBinding>(),
    RecyclerViewItemClickListener,
    KConnectionCheck.ConnectionStatusChangeListener {

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
            binding.swipeRefresh.isEnabled = false
            if (state == SCROLL_STATE_IDLE) {
                binding.swipeRefresh.isEnabled = true
                binding.dotsIndicator.selection = mPosition
                if (!isLoading && mPosition == surveyList.size - 1 && !endOfList) {
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

        binding.swipeRefresh.setOnRefreshListener {
            binding.tvTitle.text = ""
            binding.tvDescription.text = ""
            getSurveyList(1)
        }

        binding.btnRetry.setOnClickListener {
            binding.tvTitle.text = ""
            binding.tvDescription.text = ""
            getSurveyList(1)
        }

        binding.tvDate.text = DateUtil.getBeautifiedCurrentDate()

        adapter = SurveyAdapter(this)
        binding.vpSurveys.adapter = adapter
        binding.vpSurveys.setPageTransformer(FadeInOutPageTransformer())
        binding.vpSurveys.registerOnPageChangeCallback(onPageChangeCallback)

        observeSurveyList()
        getSurveyList(1)

        observeUserDetail()
        viewModel.getUserDetail()

        KConnectionCheck.addConnectionCheck(this, this, this)
    }

    private fun updateTexts(position: Int) {
        if (surveyList.size > position) {
            val survey = surveyList[position]
            binding.tvTitle.text = survey.attributes?.title
            binding.tvDescription.text = survey.attributes?.description
        }
    }

    private fun getSurveyList(page: Int) {
        if (page == 1) {
            binding.swipeRefresh.isRefreshing = false
            pageNumber = 1
            endOfList = false
            surveyList.clear()
            adapter.clearData()
        }
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

                    if (surveyList.size > 0) {
                        binding.dotsIndicator.count = surveyList.size
                        if (pageNumber == 1)
                            updateTexts(0)
                    }
                    binding.swipeRefresh.isRefreshing = false

                    changeErrorView(View.GONE)
                }
                is ResourceState.Error -> {
                    handleError(it.error)
                    binding.swipeRefresh.isRefreshing = false
                }
                is ResourceState.GenericError -> {
                    if (pageNumber > 1 && it.code == ERROR_CODE_404)
                        endOfList = true
                    else
                        handleError(it.error)
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.NetworkError -> {
                    handleError(NETWORK_ERROR)
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.Loading -> {
                    showLoading()
                    if (pageNumber != 1)
                        binding.swipeRefresh.isRefreshing = true
                }
                ResourceState.EndReach -> {
                    endOfList = true
                    binding.swipeRefresh.isRefreshing = false
                }
                ResourceState.ProtocolError -> {
                    showSessionExpire()
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun showSessionExpire() {
        // Too many follow-up requests error was thrown
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.txt_session_is_expired))
            .setCancelable(false)
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                preferenceManager.deleteAllData()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
            .show()
    }

    private fun observeUserDetail() {
        viewModel.userLiveData.observe(this) {
            when (it) {
                is ResourceState.Success -> {
                    loadImage(it.successData.attributes?.avatarUrl, binding.ivUser)
                    changeErrorView(View.GONE)
                }
                is ResourceState.Error -> handleError(it.error)
                is ResourceState.GenericError -> handleError(it.error)
                ResourceState.NetworkError -> handleError(NETWORK_ERROR)
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
        binding.tvError.text = err
        changeErrorView(View.VISIBLE)
        pageNumber--
        showLoading(false)
    }
    private fun changeErrorView(visibility: Int) {
        binding.llError.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vpSurveys.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onItemClick(position: Int) {
        SurveyDetailActivity.start(this@HomeActivity)
    }

    override fun onConnectionStatusChange(status: Boolean) {
    }
}
