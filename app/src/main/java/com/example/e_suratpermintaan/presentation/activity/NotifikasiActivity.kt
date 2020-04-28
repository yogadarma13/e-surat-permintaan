package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.ReadNotifikasi
import com.e_suratpermintaan.core.domain.entities.responses.DataNotifikasi
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.e_suratpermintaan.core.domain.entities.responses.ReadItem
import com.e_suratpermintaan.core.domain.entities.responses.UnreadItem
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.adapter.NotifikasiAdapter
import com.example.e_suratpermintaan.presentation.adapter.NotifikasiAdapter.Companion.ITEM_A
import com.example.e_suratpermintaan.presentation.adapter.NotifikasiAdapter.Companion.ITEM_B
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.NotifikasiViewModel
import kotlinx.android.synthetic.main.activity_notifikasi.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotifikasiActivity : BaseActivity() {

    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val readNotifikasiViewModel: NotifikasiViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private lateinit var notifikasiAdapter: NotifikasiAdapter
    private var idUser: String? = null

    override fun layoutId(): Int = R.layout.activity_notifikasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

    }

    private fun init() {
        idUser = profilePreference.getProfile()?.id
        notifikasiAdapter = NotifikasiAdapter()
        getNotifikasi(idUser)
        setupListeners()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notifikasiAdapter
    }

    private fun setupListeners() {
        notifikasiAdapter.setOnClickListener(object : NotifikasiAdapter.OnClickItemListener {
            override fun onClick(view: View, item: Any) {
                val dataClick = item

                val intent =
                    Intent(this@NotifikasiActivity, DetailSuratPermintaanActivity::class.java)
                if (dataClick is UnreadItem) {
                    toastNotify("Unread")
                    disposable = readNotifikasiViewModel.readNotifikasi(
                        ReadNotifikasi(
                            idUser.toString(),
                            dataClick.id.toString()
                        )
                    )
                        .subscribe()
                    intent.putExtra("id_sp", dataClick.id_sp)
                } else if (dataClick is ReadItem) {
                    toastNotify("Read")
                    intent.putExtra("id_sp", dataClick.id_sp)
                }
                startActivity(intent)
                finish()
            }
        })
    }

    private fun getNotifikasi(idUser: String?) {
        disposable = notifikasiViewModel.getNotifikasiList(idUser.toString())
            .subscribe(this::notifikasiResponse, this::handleError)
    }

    private fun notifikasiResponse(response: NotifikasiResponse) {
        var dataNotif: DataNotifikasi? = DataNotifikasi()

        response.data?.forEach {
            dataNotif = it
        }

        if (dataNotif?.countUnread == 0) {
            constraintCountUnread.visibility = View.GONE
        } else {
            constraintCountUnread.visibility = View.VISIBLE
            tvCountNotifUnread.text = "${dataNotif?.countUnread.toString()} notifikasi baru"
        }

        dataNotif?.unread?.forEach {
            it?.let { it1 ->
                notifikasiAdapter.notifList.add(it1)
                notifikasiAdapter.viewType.add(ITEM_A)

            }
        }

        dataNotif?.read?.forEach {
            it?.let { it1 ->
                notifikasiAdapter.notifList.add(it1)
                notifikasiAdapter.viewType.add(ITEM_B)
            }
        }

        notifikasiAdapter.notifyDataSetChanged()

    }
}