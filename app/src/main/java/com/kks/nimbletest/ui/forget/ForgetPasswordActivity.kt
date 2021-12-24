package com.kks.nimbletest.ui.forget

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.kks.nimbletest.R
import com.kks.nimbletest.data.network.ResourceState
import com.kks.nimbletest.databinding.ActivityForgetPasswordBinding
import com.kks.nimbletest.ui.base.BaseViewBindingActivity
import com.kks.nimbletest.util.NETWORK_ERROR
import com.kks.nimbletest.util.extensions.hideKeyboard
import com.kks.nimbletest.util.extensions.toast
import com.kks.nimbletest.viewmodel.forget.ForgetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by kaungkhantsoe at 22/12/2021
 */

@AndroidEntryPoint
class ForgetPasswordActivity: BaseViewBindingActivity<ActivityForgetPasswordBinding>() {

    lateinit var viewModel: ForgetPasswordViewModel

    override val bindingInflater: (LayoutInflater) -> ActivityForgetPasswordBinding
        get() = ActivityForgetPasswordBinding::inflate

    override fun setup() {

        viewModel = ViewModelProvider(this)[ForgetPasswordViewModel::class.java]
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar,true)
        supportActionBar?.title = ""
        binding.toolbar.toolbar.background = null

        binding.btnReset.setOnClickListener {
            hideKeyboard()
            binding.btnReset.isEnabled = false
            viewModel.sendForgetPasswordEmail(binding.etEmail.text.toString())
        }

        observeForgetPassword()
    }

    private fun observeForgetPassword() {
        viewModel.forgetPasswordLiveData.observe(this) {
            when(it) {
                is ResourceState.Loading -> {
                    binding.spinKit.visibility = View.VISIBLE
                    binding.btnReset.isEnabled = false
                }
                is ResourceState.Error -> handleError(it.error)
                is ResourceState.GenericError -> handleError(it.error)
                ResourceState.NetworkError -> handleError(NETWORK_ERROR)
                is ResourceState.Success -> {
                    binding.spinKit.visibility = View.INVISIBLE
                    showDialog(it.successData)
                }
                else -> {
                    // do nothing
                    binding.btnReset.isEnabled = true
                }
            }
        }
    }

    private fun showDialog(msg: String) {
        //Too many follow-up requests error was thrown
        AlertDialog.Builder(this)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok
            ) { dialog, id ->
                finish()
            }
            .show()
    }

    private fun handleError(err: String?) {
        toast(err ?: "")
        binding.spinKit.visibility = View.INVISIBLE
        binding.btnReset.isEnabled = true
    }

}