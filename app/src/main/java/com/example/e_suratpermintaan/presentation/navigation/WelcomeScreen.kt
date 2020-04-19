package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.helpers.NavigationHelper
import kotlinx.android.synthetic.main.fragment_welcome_screen.*

/**
 * A simple [Fragment] subclass.
 */
class WelcomeScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext.setOnClickListener {
            it.findNavController().navigate(R.id.action_welcomeScreen_to_loginFragment, null, NavigationHelper.getNavOptions())
        }
    }

}
