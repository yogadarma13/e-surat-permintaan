package com.example.e_suratpermintaan.external.helpers

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.e_suratpermintaan.R

object WindowHelper {

    // https://stackoverflow.com/questions/59215711/fullscreen-fragment-with-transparent-status-bar-programmatically
    fun transparentStatusBar(
        activity: Activity,
        isTransparent: Boolean,
        fullscreen: Boolean
    ) {
        if (isTransparent) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            // (activity as AppCompatActivity).supportActionBar!!.hide()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // defaultStatusBarColor = activity.window.statusBarColor
                activity.window
                    .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                // FOR TRANSPARENT NAVIGATION BAR
                // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                activity.window.statusBarColor = Color.TRANSPARENT
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    activity.window
                        .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                }
            }
        } else {
            if (fullscreen) {
                val decorView = activity.window.decorView
                val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
                decorView.systemUiVisibility = uiOptions
            } else {
                (activity as AppCompatActivity).supportActionBar?.show()
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.window
                        .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                    activity.window
                        .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    activity.window
                        .clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    activity.window.statusBarColor =
                        activity.resources.getColor(R.color.colorPrimaryDark)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        activity.window
                            .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    }
                }
            }
        }
    }

}