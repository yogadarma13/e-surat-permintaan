package com.example.e_suratpermintaan.presentation.navigation

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.helpers.NavOptionsHelper
import com.example.e_suratpermintaan.external.helpers.WindowHelper.transparentStatusBar
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */

class SplashScreen : BaseFragment() {

    lateinit var handler: Handler
    val profilePreference: ProfilePreference by inject()

    override fun layoutId(): Int = R.layout.fragment_splash_screen

    val runnable = Runnable {
        if (profilePreference.getProfile() == null) {
            val navOptions =
                NavOptionsHelper.getInstance().addAppStarterAnim().clearBackStack(R.id.splashScreen)
                    .build()
            view?.findNavController()?.navigate(
                R.id.action_splashScreen_to_welcomeScreen,
                null,
                navOptions
            )
        } else {
            val navOptions =
                NavOptionsHelper.getInstance().addLoginToMainAnim()
                    .clearBackStack(R.id.splashScreen).build()
            view?.findNavController()?.navigate(
                R.id.action_splashScreen_to_mainFragment,
                null,
                navOptions
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
    }

    override fun onStart() {
        super.onStart()

        handler.postDelayed(runnable, 2000)
    }

    override fun onResume() {
        super.onResume()
        transparentStatusBar(activity as Activity, true, false)
    }

    override fun onStop() {
        super.onStop()
        transparentStatusBar(activity as Activity, false, false)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

}
