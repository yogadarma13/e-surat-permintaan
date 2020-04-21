package com.example.e_suratpermintaan.presentation.navigation

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.helpers.NavOptionsHelper
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.viewmodel.AuthViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class LoginFragment : BaseFragment() {

    private val profileViewModel: ProfileViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    override fun layoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            disposable = authViewModel
                .doLogin(Login(etEmail.text.toString(), etPassword.text.toString()))
                .subscribe(this::loginResponse, this::handleError)
        }, 2000)

        closeKeyboard(activity as Activity)
    }

    private fun loginResponse(response: LoginResponse) {
        val dataLogin = response.data

        dataLogin?.id?.let { id ->

            disposable = profileViewModel.getProfile(id)
                .subscribe(
                    { profileResponse ->

                        var dataProfile: DataProfile? = DataProfile()

                        profileResponse.data?.forEach { it ->
                            dataProfile = it
                        }

                        progressBarOverlay.visibility = GONE

                        if (dataProfile != null) {
                            profilePreference.saveProfile(dataProfile)

                            val navOptions =
                                NavOptionsHelper.getInstance().addLoginToMainAnim()
                                    .clearBackStack(R.id.mainFragment).build()
                            view?.findNavController()
                                ?.navigate(
                                    R.id.action_loginFragment_to_mainFragment,
                                    null,
                                    navOptions
                                )

                            toastNotify(response.message)
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

    private fun handleError(error: Throwable) {
        toastNotify(error.message.toString())
    }

}
