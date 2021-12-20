package com.kks.nimbletest.ui.login

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.kks.nimbletest.constants.AppConstants
import com.kks.nimbletest.constants.PrefConstants
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.databinding.ActivityLoginBinding
import com.kks.nimbletest.ui.base.BaseViewBindingActivity
import com.kks.nimbletest.util.PreferenceManager
import com.kks.nimbletest.util.extensions.toast
import com.kks.nimbletest.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by kaungkhantsoe at 18/12/2021
 */

@AndroidEntryPoint
class LoginActivity : BaseViewBindingActivity<ActivityLoginBinding>() {

    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun setup() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        observeLogin()

        binding.btnLogIn.setOnClickListener {
            viewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun observeLogin() {
        viewModel.loginLiveData.observe(this) { state ->
            when (state) {
                is ResourceState.Loading -> binding.btnLogIn.isEnabled = false
                is ResourceState.Error -> handleError(state.error)
                is ResourceState.GenericError -> handleError(state.error)
                ResourceState.NetworkError -> handleError(AppConstants.NETWORK_ERROR)
                is ResourceState.Success -> {

                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun handleError(err: String?) {
        toast(err ?: "")
        binding.btnLogIn.isEnabled = true
    }
}