package com.example.e_suratpermintaan.presentation.navigation

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.FragmentLoginBinding
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.activity.MainActivity
import com.example.e_suratpermintaan.presentation.activity.StarterActivity
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.dialog.ProgressBarDialog
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewmodel.AuthViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val profileViewModel: ProfileViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    private val sharedMasterData: SharedMasterData by inject()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

    private lateinit var progressBarDialog: ProgressBarDialog

//    override fun layoutId(): Int = R.layout.fragment_login

    override fun onEnterAnimationEnd() {
        super.onEnterAnimationEnd()

        progressBarDialog = ProgressBarDialog()

        binding.passwordSeek.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.etPassword.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            // Ketika tombol enter di keyboard ditekan
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.etPassword.clearFocus()
                closeKeyboard(activity as Activity)
                doLogin()
                return@OnKeyListener true
            }
            false
        })

        binding.btnSignin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        showProgressBar()
        Handler(Looper.getMainLooper()).postDelayed({
            fcmPreference.getUserTokenId()?.let { userTokenId ->
                disposable = authViewModel
                    .doLogin(
                        Login(
                            binding.etUsername.text.toString(),
                            binding.etPassword.text.toString(),
                            userTokenId
                        )
                    )
                    .subscribe(this::loginResponse, this::handleError)
                closeKeyboard(requireActivity())
            }
        }, 850)

        closeKeyboard(activity as Activity)
    }

    private fun showProgressBar() {
        if (!progressBarDialog.isAdded) {
            progressBarDialog.show(childFragmentManager, tag)
        }
    }

    private fun dismissProgressBar() {
        progressBarDialog.dismiss()
    }

    private fun loginResponse(response: LoginResponse) {
        val dataLogin = response.data

        val starterActivity = (requireActivity() as StarterActivity)

        dataLogin?.id?.let { id ->

            disposable = profileViewModel.getProfile(id)
                .subscribe(
                    { profileResponse ->

                        var dataProfile: DataProfile? = DataProfile()

                        profileResponse.data?.forEach { it ->
                            dataProfile = it
                        }

                        if (dataProfile != null) {
                            profilePreference.saveProfile(dataProfile)

                            starterActivity.initUserDataDependentApiRequest()
                            sharedMasterData.isAllMasterObservableResponseComplete.observe(
                                starterActivity,
                                { isIt ->

                                    if (isIt) {
                                        toastNotify(response.message)
                                        dismissProgressBar()

                                        Handler(Looper.getMainLooper()).postDelayed({
                                            val intent =
                                                Intent(starterActivity, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            starterActivity.startActivity(
                                                intent
                                            )
                                            starterActivity.finish()
                                        }, 500)
                                    }

                                })
                        } else {
                            toastNotify(getString(R.string.profile_get_error_message))
                        }

                    },
                    { error ->
                        dismissProgressBar()
                        toastNotify(error.message.toString())
                    }
                )
        }
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        dismissProgressBar()
    }

}
