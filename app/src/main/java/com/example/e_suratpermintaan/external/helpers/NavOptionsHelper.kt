package com.example.e_suratpermintaan.external.helpers

import androidx.navigation.NavOptions
import com.example.e_suratpermintaan.R

class NavOptionsHelper {

    private var builder: NavOptions.Builder = NavOptions.Builder()

    companion object {
        fun getInstance(): NavOptionsHelper = NavOptionsHelper()
    }

    fun addAnim(): NavOptionsHelper {
        builder = builder
            .setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.slide_out_to_left)
            .setPopEnterAnim(R.anim.slide_in_from_left)
            .setPopExitAnim(R.anim.slide_out_to_right)
        return this
    }

    fun clearBackStack(fragmentIdToClear: Int):NavOptionsHelper {
        builder = builder
            .setLaunchSingleTop(true)
            .setPopUpTo(fragmentIdToClear, true)
        return this
    }

    fun build(): NavOptions {
       return builder.build()
    }
}