package com.example.e_suratpermintaan.presentation.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.FragmentSplashBinding
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.framework.utils.NavOptionsHelper
import com.example.e_suratpermintaan.framework.utils.WindowHelper.transparentStatusBar
import com.example.e_suratpermintaan.presentation.activity.MainActivity
import com.example.e_suratpermintaan.presentation.activity.StarterActivity
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.google.firebase.iid.FirebaseInstanceId
import org.koin.android.ext.android.inject
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private lateinit var handler: Handler

    private val sharedMasterData: SharedMasterData by inject()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

//    override fun layoutId(): Int = R.layout.fragment_splash

    private val runnable = Runnable {
        if (fcmPreference.getUserTokenId() == null) {
            Log.d("FCM", "REQUEST NEW TOKEN")
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    if (task.exception is IOException) {
                        toastNotify("Maaf, Request Token Gagal \nAnda sedang offline, silakan hidupkan sambungan internet")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Firebase token registration request error : ${task.exception}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    return@addOnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                fcmPreference.saveUserTokenId(token)

                startTaskOnTokenCheckComplete()
            }
        } else {
            startTaskOnTokenCheckComplete()
        }

        val msg = getString(R.string.msg_token_fmt, fcmPreference.getUserTokenId())
        Log.d("FCM", msg)
    }

    private fun startTaskOnTokenCheckComplete() {
        if (profilePreference.getProfile() == null) {
            val navOptions =
                NavOptionsHelper.getInstance().addAppStarterAnim()
                    .clearBackStack(R.id.splashFragment)
                    .build()
            view?.findNavController()?.navigate(
                R.id.action_splashScreen_to_welcomeScreen,
                null,
                navOptions
            )
        } else {
            val starterActivity = (requireActivity() as StarterActivity)

            sharedMasterData.isAllMasterObservableResponseComplete.observe(
                starterActivity,
                { isIt ->

                    if (isIt) {
                        requireActivity().startActivity(
                            Intent(requireActivity(), MainActivity::class.java)
                        )
                        requireActivity().finish()
                    }

                })

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
    }

    override fun onStart() {
        super.onStart()

        handler.postDelayed(runnable, 1000)
    }

    override fun onResume() {
        super.onResume()
        transparentStatusBar(activity as Activity, isTransparent = true, fullscreen = false)
    }

    override fun onStop() {
        super.onStop()
        transparentStatusBar(activity as Activity, isTransparent = false, fullscreen = false)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

}
