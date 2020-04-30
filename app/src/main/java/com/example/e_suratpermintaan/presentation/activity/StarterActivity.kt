package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import androidx.navigation.ActivityNavigator
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseActivity

class StarterActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_starter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initApiRequest()
    }

    private fun initApiRequest() {

    }

    override fun finish() {
        super.finish()
        // https://developer.android.com/guide/navigation/navigation-animate-transitions#apply_pop_animations_to_activity_transitions
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

}
