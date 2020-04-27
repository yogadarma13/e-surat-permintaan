package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileActivity : BaseActivity() {

    private val provileViewModel: ProfileViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

    override fun layoutId(): Int = R.layout.activity_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idUser = profilePreference.getProfile()?.id
        getDataProfile(idUser)

        btnLogout.setOnClickListener {
            profilePreference.removeProfile()
            fcmPreference.removeUserTokenId()

            finish()
            startActivity(Intent(this@ProfileActivity, StarterActivity::class.java))
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

}
