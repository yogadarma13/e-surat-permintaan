package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.utils.NavOptionsHelper
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_welcome

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext.setOnClickListener {
            val navOptions =
                NavOptionsHelper.getInstance().addAppStarterAnim().build()
            it.findNavController()
                .navigate(R.id.action_welcomeScreen_to_loginFragment, null, navOptions)
        }
    }

    override fun onEnterAnimationEnd() {

    }

}
