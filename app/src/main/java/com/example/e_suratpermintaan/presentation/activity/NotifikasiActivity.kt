package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.DataNotifikasi
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.adapter.NotifikasiAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.NotifikasiViewModel
import kotlinx.android.synthetic.main.activity_notifikasi.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotifikasiActivity : BaseActivity() {

    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    override fun layoutId(): Int = R.layout.activity_notifikasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idUser = profilePreference.getProfile()?.id

        getNotifikasi(idUser)

    }

    private fun getNotifikasi(idUser: String?){
        disposable = notifikasiViewModel.getNotifikasiList(idUser.toString())
            .subscribe(this::notifikasiResponse, this::handleError)
    }

    private fun notifikasiResponse(response: NotifikasiResponse){
        var dataNotif: DataNotifikasi? = DataNotifikasi()
        response.data?.forEach {
            dataNotif = it
        }

        val dataAllNotif = arrayListOf<Any>()

        dataNotif?.read?.forEach {
            it?.let { it1 -> dataAllNotif.add(it1) }
        }

        dataNotif?.unread?.forEach {
            it?.let { it1 -> dataAllNotif.add(it1) }
        }

//        toastNotify(dataAllNotif.size.toString())

        recyclerView.layoutManager = LinearLayoutManager(this)

        val notifikasiAdapter = NotifikasiAdapter(dataAllNotif)

        recyclerView.adapter = notifikasiAdapter
    }

    private fun handleError(error: Throwable) {
        toastNotify(error.message.toString())
    }

}
