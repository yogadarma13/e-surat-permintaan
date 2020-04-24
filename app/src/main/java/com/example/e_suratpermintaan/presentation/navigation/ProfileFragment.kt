package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.helpers.NavOptionsHelper
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment() {

    private val provileViewModel: ProfileViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    override fun layoutId(): Int = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idUser = profilePreference.getProfile()?.id
        getDataProfile(idUser)

        btnLogout.setOnClickListener {
            profilePreference.removeProfile()

            val navOptions =
                NavOptionsHelper.getInstance().addBackToSplashAnim()
                    .clearBackStack(R.id.welcomeFragment).build()
            it.findNavController()
                .navigate(R.id.action_profileFragment_to_splashFragment, null, navOptions)
        }
    }

    private fun getDataProfile(id_user: String?) {
        disposable = provileViewModel.getProfile(id_user.toString())
            .subscribe(this::profileResponse, this::handleError)
    }

    private fun profileResponse(response: ProfileResponse) {
        var dataProfile: DataProfile? = DataProfile()

        response.data?.forEach {
            dataProfile = it
        }

        tv_name_profile.text = dataProfile?.name
        tv_email_profile.text = dataProfile?.email

    }

    private fun handleError(error: Throwable) {
        toastNotify(error.message.toString())
    }

}
