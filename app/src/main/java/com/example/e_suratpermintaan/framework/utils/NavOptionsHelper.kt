package com.example.e_suratpermintaan.framework.utils

import androidx.navigation.NavOptions
import com.example.e_suratpermintaan.R

class NavOptionsHelper {

    private var builder: NavOptions.Builder = NavOptions.Builder()

    companion object {
        fun getInstance(): NavOptionsHelper =
            NavOptionsHelper()
    }

    fun addDefaultAnim(): NavOptionsHelper {
        builder = builder
            .setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.slide_out_to_left)
            .setPopEnterAnim(R.anim.slide_in_from_left)
            .setPopExitAnim(R.anim.slide_out_to_right)
        return this
    }

    fun addAppStarterAnim(): NavOptionsHelper {
        builder = builder
            .setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.slide_out_to_left)
            .setPopEnterAnim(R.anim.slide_in_from_left)
            .setPopExitAnim(R.anim.slide_out_to_right)
        return this
    }

    fun addBackToSplashAnim(): NavOptionsHelper {
        builder = builder
            .setEnterAnim(R.anim.slide_in_from_left)
            .setExitAnim(R.anim.slide_out_to_right)
        return this
    }

    fun addLoginToMainAnim(): NavOptionsHelper {
        builder = builder
            .setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.slide_out_login_to_main)
            .setPopEnterAnim(R.anim.slide_in_from_left)
            .setPopExitAnim(R.anim.slide_out_to_right)
        return this
    }

    fun clearBackStack(fragmentIdToClear: Int): NavOptionsHelper {
        builder = builder
            .setLaunchSingleTop(true)
            .setPopUpTo(fragmentIdToClear, true)
        return this
    }

    fun build(): NavOptions {
       return builder.build()
    }
}