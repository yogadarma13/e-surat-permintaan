package com.example.e_suratpermintaan.external.helpers

import androidx.navigation.NavOptions
import com.example.e_suratpermintaan.R

object NavigationHelper {
    fun getNavOptions(): NavOptions? {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.slide_out_to_left)
            .setPopEnterAnim(R.anim.slide_in_from_left)
            .setPopExitAnim(R.anim.slide_out_to_right)
            .build()
    }
}