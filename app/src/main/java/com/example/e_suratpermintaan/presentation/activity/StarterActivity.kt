package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.navigation.ActivityNavigator
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import io.reactivex.rxjava3.core.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StarterActivity : BaseActivity() {

    private var profileId: String? = null
    private lateinit var idUser: String

    private val masterViewModel: MasterViewModel by viewModel()
    private val sharedMasterData: SharedMasterData by inject()
    private val profilePreference: ProfilePreference by inject()

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

        val observableUom = masterViewModel.getUomList("all")

        val observableKategori = masterViewModel.getKategoriList()

        val observablePenugasan = masterViewModel.getPenugasanOptionList()

        val observableStatusPenugasan = masterViewModel.getStatusPenugasanOptionList()

        val observablePersyaratan = masterViewModel.getPersyaratanList("all")

        val observableJenisDataFilter = masterViewModel.getJenisDataFilterOptionList()

        val observableStatusFilter = masterViewModel.getStatusFilterOptionList(idUser)

        val observableProyekFilter = masterViewModel.getProyekFilterOptionList(idUser)

        val observableJenisPermintaanFilter =
            masterViewModel.getJenisPermintaanFilterOptionList(idUser)

        val concat1 = Observable.concat(
            observableCC.onErrorResumeNext { Observable.empty() },
            observableUom.onErrorResumeNext { Observable.empty() },
            observableKategori.onErrorResumeNext { Observable.empty() },
            observablePersyaratan.onErrorResumeNext { Observable.empty() })

        val concat2 = Observable.concat(
            observablePenugasan.onErrorResumeNext { Observable.empty() },
            observableStatusPenugasan.onErrorResumeNext { Observable.empty() }
        )

        val concat3 = Observable.concat(
            observableJenisDataFilter.onErrorResumeNext { Observable.empty() },
            observableStatusFilter.onErrorResumeNext { Observable.empty() },
            observableProyekFilter.onErrorResumeNext { Observable.empty() },
            observableJenisPermintaanFilter.onErrorResumeNext { Observable.empty() })

        disposable = Observable.concat(
            concat1.onErrorResumeNext { Observable.empty() },
            concat2.onErrorResumeNext { Observable.empty() },
            concat3.onErrorResumeNext { Observable.empty() })
            .subscribe(this::handleResponse, this::handleError)

        // Master API END
        // -----------------------------------------------------------
    }

    fun initUserDataDependentApiRequest() {
        profileId = profilePreference.getProfile()?.id
        idUser = profileId.toString()

        // Master request api - START
        // -----------------------------------------------------------
        val observableStatusFilter = masterViewModel.getStatusFilterOptionList(idUser)

        val observableProyekFilter = masterViewModel.getProyekFilterOptionList(idUser)

        val observableJenisPermintaanFilter =
            masterViewModel.getJenisPermintaanFilterOptionList(idUser)

        disposable = Observable.concat(
            observableStatusFilter.onErrorResumeNext { Observable.empty() },
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
                sharedMasterData.setCostCodeList(response.data)
                Log.d("MYAPPSTARTER", "CC RESPONSE")
            }

            is MasterUOMResponse -> {
                sharedMasterData.setUomList(response.data)
                Log.d("MYAPPSTARTER", "UOM RESPONSE")
            }

            is KategoriResponse -> {
                sharedMasterData.setKategoriList(response.data)
                Log.d("MYAPPSTARTER", "KATEGORI RESPONSE")
            }

            is MasterPersyaratanResponse -> {
                sharedMasterData.setPersyaratanList(response.data)
                Log.d("MYAPPSTARTER", "PERSYARATAN RESPONSE")
            }

            // PENUGASAN
            // -----------------------------------------------------------
            is MasterPenugasanOptionResponse -> {
                sharedMasterData.setPenugasanList(response.data)
            }

            is MasterStatusPenugasanOptionResponse -> {
                sharedMasterData.setStatusPenugasanList(response.data)
            }

            // Filter Option List
            // -----------------------------------------------------------

            is MasterJenisDataFilterOptionResponse -> {
                sharedMasterData.setJenisDataFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "JENIS DATA FILTER RESPONSE")
            }

            is MasterStatusFilterOptionResponse -> {
                sharedMasterData.setStatusFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "STATUS FILTER RESPONSE")
            }

            is MasterProyekFilterOptionResponse -> {
                sharedMasterData.setProyekFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "PROYEK FILTER RESPONSE")
            }

            is MasterJenisPermintaanFilterOptionResponse -> {
                sharedMasterData.setJenisPermintaanFilterOptionList(response.data)
                Log.d("MYAPPSTARTER", "JENIS PERMINTAAN FILTER RESPONSE")

                if (sharedMasterData.isAllMasterObservableResponseComplete.value == null)
                    sharedMasterData.isAllMasterObservableResponseComplete.value = true
            }
        }
    }

    override fun finish() {
        super.finish()
        // https://developer.android.com/guide/navigation/navigation-animate-transitions#apply_pop_animations_to_activity_transitions
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

}
