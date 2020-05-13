package com.example.e_suratpermintaan.presentation.navigation

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.activity.MainActivity
import com.example.e_suratpermintaan.presentation.activity.StarterActivity
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.viewmodel.AuthViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedMasterViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class LoginFragment : BaseFragment() {

    private val profileViewModel: ProfileViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    private val sharedMasterViewModel: SharedMasterViewModel by inject()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

    override fun layoutId(): Int = R.layout.fragment_login

    override fun onEnterAnimationEnd() {
        super.onEnterAnimationEnd()

        passwordSeek.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        etPassword.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            // Ketika tombol enter di keyboard ditekan
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                etPassword.clearFocus()
                closeKeyboard(activity as Activity)
                doLogin()
                return@OnKeyListener true
            }
            false
        })

        btnSignin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        progressBarOverlay.visibility = VISIBLE
        Handler().postDelayed({
            fcmPreference.getUserTokenId()?.let { userTokenId ->
                disposable = authViewModel
                    .doLogin(
                        Login(
                            etEmail.text.toString(),
                            etPassword.text.toString(),
                            userTokenId
                        )
                    )
                    .subscribe(this::loginResponse, this::handleError)
                closeKeyboard(requireActivity())
            }
        }, 850)

        closeKeyboard(activity as Activity)
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
                            sharedMasterViewModel.isAllMasterObservableResponseComplete.observe(
                                starterActivity,
                                Observer { isIt ->

                                    if (isIt) {
                                        toastNotify(response.message)
                                        progressBarOverlay.visibility = GONE

                                        Handler().postDelayed({
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
                        progressBarOverlay.visibility = GONE
                        toastNotify(error.message.toString())
                    }
                )
        }
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        progressBarOverlay.visibility = GONE
    }

}
