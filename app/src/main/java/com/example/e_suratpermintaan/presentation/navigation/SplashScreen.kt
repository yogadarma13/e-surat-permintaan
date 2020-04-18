package com.example.e_suratpermintaan.presentation.navigation

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.helpers.NavigationHelper
import com.example.e_suratpermintaan.external.helpers.WindowHelper.transparentStatusBar
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */

class SplashScreen : Fragment() {

    lateinit var handler: Handler
    val profilePreference: ProfilePreference by inject()

    val runnable = Runnable {
        if (profilePreference.getProfile() == null) {
            view?.findNavController()?.navigate(R.id.action_splashScreen_to_loginFragment, null, NavigationHelper.getNavOptions())
        } else {
            view?.findNavController()?.navigate(R.id.action_splashScreen_to_mainFragment, null, NavigationHelper.getNavOptions())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
