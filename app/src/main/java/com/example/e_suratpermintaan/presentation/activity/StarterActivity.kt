package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.ActivityNavigator
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import io.reactivex.rxjava3.core.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StarterActivity : BaseActivity() {

    private var profileId: String? = null
    private lateinit var idUser: String

    private val masterViewModel: MasterViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by inject()
    private val profilePreference: ProfilePreference by inject()

    var isAllObservableComplete = false
    var isSplashRequestStartMainActivity = false
    var isLoginRequestStartMainActivity = false

    var loginResponse: String = ""

    override fun layoutId(): Int = R.layout.activity_starter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initApiRequest()
    }

    fun initApiRequest() {

        profileId = profilePreference.getProfile()?.id
        idUser = profileId.toString()

        // Master request api - START
        // -----------------------------------------------------------
        val observableCC = masterViewModel.getCostCodeList("all")

        val observableUom =masterViewModel.getUomList("all")

        val observablePersyaratan = masterViewModel.getPersyaratanList("all")

        val observableStatusFilter = masterViewModel.getStatusFilterOptionList()

        val observableJenisDataFilter = masterViewModel.getJenisDataFilterOptionList()

        val observableProyekFilter = masterViewModel.getProyekFilterOptionList(idUser)

        val observableJenisPermintaanFilter = masterViewModel.getJenisPermintaanFilterOptionList(idUser)

        val concat1 = Observable.concat(
            observableCC.onErrorResumeNext { Observable.empty() },
            observableUom.onErrorResumeNext { Observable.empty() },
            observablePersyaratan.onErrorResumeNext { Observable.empty() })

        val concat2 = Observable.concat(
            observableStatusFilter.onErrorResumeNext { Observable.empty() },
            observableJenisDataFilter.onErrorResumeNext { Observable.empty() },
            observableProyekFilter.onErrorResumeNext { Observable.empty() },
            observableJenisPermintaanFilter.onErrorResumeNext { Observable.empty() })

        disposable = Observable.concat(
            concat1.onErrorResumeNext { Observable.empty() },
            concat2.onErrorResumeNext { Observable.empty() })
            .subscribe(this::handleResponse, this::handleError)

        // Master API END
        // -----------------------------------------------------------
    }

    fun initUserDataDependentApiRequest(){
        profileId = profilePreference.getProfile()?.id
        idUser = profileId.toString()

        // Master request api - START
        // -----------------------------------------------------------

        val observableProyekFilter = masterViewModel.getProyekFilterOptionList(idUser)

        val observableJenisPermintaanFilter = masterViewModel.getJenisPermintaanFilterOptionList(idUser)

        disposable = Observable.concat(
            observableProyekFilter.onErrorResumeNext { Observable.empty() },
            observableJenisPermintaanFilter.onErrorResumeNext { Observable.empty() })
            .subscribe(this::handleResponse, this::handleError)

        // Master API END
        // -----------------------------------------------------------
    }

    private fun handleResponse(response: Any) {
        when (response) {

            // CRUD ITEM Field List
            // -----------------------------------------------------------
            is MasterCCResponse -> {
                sharedViewModel.setCostCodeList(response.data)
                Log.d("MYAPPSTARTER", "CC RESPONSE")
            }

            is MasterUOMResponse -> {
                sharedViewModel.setUomList(response.data)
                Log.d("MYAPPSTARTER", "UOM RESPONSE")
            }

            is MasterPersyaratanResponse -> {
                sharedViewModel.setPersyaratanList(response.data)
                Log.d("MYAPPSTARTER", "PERSYARATAN RESPONSE")
            }

            // Filter Option List
            // -----------------------------------------------------------
            is MasterStatusFilterOptionResponse -> {
                sharedViewModel.setStatusFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "STATUS FILTER RESPONSE")
            }

            is MasterJenisDataFilterOptionResponse -> {
                sharedViewModel.setJenisDataFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "JENIS DATA FILTER RESPONSE")
            }

            is MasterProyekFilterOptionResponse -> {
                sharedViewModel.setProyekFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "PROYEK FILTER RESPONSE")
            }

            is MasterJenisPermintaanFilterOptionResponse -> {
                sharedViewModel.setJenisPermintaanFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "JENIS PERMINTAAN FILTER RESPONSE")

                sharedViewModel.isAllMasterObservableResponseComplete.value = true
            }
        }
    }

    override fun finish() {
        super.finish()
        // https://developer.android.com/guide/navigation/navigation-animate-transitions#apply_pop_animations_to_activity_transitions
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

}
