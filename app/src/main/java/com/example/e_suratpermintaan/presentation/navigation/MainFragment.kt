package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMyData
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.helpers.NavOptionsHelper
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class MainFragment : BaseFragment() {

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    override fun layoutId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocus()

        val profileId = profilePreference.getProfile()?.id

        if (profileId != null) {
            disposable = suratPermintaanViewModel.readAllData("21")
                .subscribe(this::handleResponse, this::handleError)
        }

        btnLogout.setOnClickListener {
            profilePreference.removeProfile()
            val navOptions =
                NavOptionsHelper.getInstance().addBackToSplashAnim()
                    .clearBackStack(R.id.splashScreen).build()
            it.findNavController()
                .navigate(R.id.action_mainFragment_to_splashScreen, null, navOptions)
        }
    }

    private fun handleResponse(response: MyDataResponse) {
        val mySuratPermintaanList: List<DataMyData?>? = response.data

    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
    }

}
