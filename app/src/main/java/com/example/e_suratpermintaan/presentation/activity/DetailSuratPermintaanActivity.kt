package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.DetailSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DetailSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.dialog.TambahItemDialog
import com.example.e_suratpermintaan.presentation.viewholders.ItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import kotlinx.android.synthetic.main.activity_detail_surat_permintaan.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailSuratPermintaanActivity : BaseActivity() {

    private lateinit var alertDialogTambah: TambahItemDialog
    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val sharedViewModel: SharedViewModel by inject()

    private var idSp: String? = null
    private var kodeSp: String? = null
    private lateinit var idUser: String
    private lateinit var itemSuratPermintaanAdapter: BaseAdapter<ItemSuratPermintaanViewHolder>

    override fun layoutId(): Int = R.layout.activity_detail_surat_permintaan

    companion object {
        const val ID_SP_EXTRA_KEY = "id_sp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString(ID_SP_EXTRA_KEY)
        val profileId = profilePreference.getProfile()?.id
        if (profileId != null) {
            idUser = profileId
        }

        init()
        setupListeners()
    }

    private fun init() {
        initRecyclerView()

        alertDialogTambah =
            TambahItemDialog(this, sharedViewModel, itemSuratPermintaanViewModel)

        disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser)
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun setupListeners() {
        tvAddItem.setOnClickListener {
            alertDialogTambah.show()
        }
    }

    private fun initRecyclerView() {

        itemSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_row,
            ItemSuratPermintaanViewHolder::class.java
        )

        itemSuratPermintaanAdapter.setOnItemClickListener {
            val item = it as ItemsDetailSP
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemSuratPermintaanAdapter

        btnHistory.setOnClickListener {
            val intent = Intent(this@DetailSuratPermintaanActivity, HistorySuratPermintaanActivity::class.java)
            intent.putExtra("id_sp", idSp)
            startActivity(intent)
        }

        disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser)
            .subscribe(this::handleResponse, this::handleError)

    }

    fun handleResponse(response: Any) {
        when (response) {
            is CreateItemSPResponse -> {

                toastNotify(response.message)
                itemSuratPermintaanAdapter.itemList.clear()
                itemSuratPermintaanAdapter.notifyDataSetChanged()
                init()

            }
            is DetailSPResponse -> {

                val detailSPResponse = response.data
                val dataDetailSP = detailSPResponse?.get(0)
                kodeSp = dataDetailSP?.kode

                alertDialogTambah.initDialogViewTambah(kodeSp.toString(), idUser)

                val detailDate = dataDetailSP?.tanggalPengajuan?.split(" ")

                tv_kode_detail.text = kodeSp
                tv_kode_sp.text = kodeSp
                tv_name_proyek_detail.text = dataDetailSP?.namaProyek
                tv_location_detail.text = dataDetailSP?.namaLokasi
                tv_date_detail.text = detailDate?.get(0)
                tv_time_detail.text = detailDate?.get(1)
                tv_status_detail.text = dataDetailSP?.statusPermintaan
                tv_jenis_detail.text = dataDetailSP?.jenis

                val itemList: List<ItemsDetailSP?>? = dataDetailSP?.items

                itemList?.forEach {
                    itemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
                }

                itemSuratPermintaanAdapter.notifyDataSetChanged()

            }
        }
    }

}
