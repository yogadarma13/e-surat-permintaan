package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DetailSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.dialog.TambahItemDialog
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.ItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_detail_surat_permintaan.*
import kotlinx.android.synthetic.main.dialog_catatan.view.*
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

        btnHistory.setOnClickListener {
            val intent = Intent(
                this@DetailSuratPermintaanActivity,
                HistorySuratPermintaanActivity::class.java
            )
            intent.putExtra("id_sp", idSp)
            startActivity(intent)
        }

        btnAjukan.setOnClickListener {

            val alertDialog =
                AlertDialog.Builder(this).setMessage("Apa anda yakin ingin mengajukan permintaan ini?")
                    .setPositiveButton("Ajukan") { _, _ ->
                        disposable = suratPermintaanViewModel.ajukan(idUser, idSp.toString())
                            .subscribe(this::handleResponse, this::handleError)
                    }.setNegativeButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

            alertDialog.show()
        }

        btnCancel.setOnClickListener {

            val alertDialog =
                AlertDialog.Builder(this).setMessage("Apa anda yakin ingin membatalkan permintaan ini?")
                    .setPositiveButton("Batalkan") { _, _ ->
                        disposable = suratPermintaanViewModel.cancel(idUser, idSp.toString())
                            .subscribe(this::handleResponse, this::handleError)
                    }.setNegativeButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

            alertDialog.show()
        }

        btnAccept.setOnClickListener {

            val alertDialog =
                AlertDialog.Builder(this).setMessage("Apa anda yakin ingin menerima permintaan ini?")
                    .setPositiveButton("Terima") { _, _ ->
                        disposable = suratPermintaanViewModel.verifikasi(idUser, idSp.toString(), "0", "")
                            .subscribe(this::handleResponse, this::handleError)
                    }.setNegativeButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

            alertDialog.show()
        }

        btnDecline.setOnClickListener {
            showDialogCatatan()
        }
    }

    private fun initRecyclerView() {

        itemSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_item_row,
            ItemSuratPermintaanViewHolder::class.java
        )

        itemSuratPermintaanAdapter.setOnItemClickListener { item, actionString ->
            val data = item as ItemsDetailSP

            when (actionString){
                BaseViewHolder.ROOTVIEW -> {

                }
                ItemSuratPermintaanViewHolder.BTN_EDIT -> {

                }
                ItemSuratPermintaanViewHolder.BTN_HAPUS -> {

                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemSuratPermintaanAdapter

        btnHistory.setOnClickListener {
            val intent = Intent(this@DetailSuratPermintaanActivity, HistorySuratPermintaanActivity::class.java)
            intent.putExtra("id_sp", idSp)
            startActivity(intent)
        }

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

                alertDialogTambah.initDialogViewTambah(kodeSp.toString(), idUser, dataDetailSP?.jenis.toString())

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

                if (dataDetailSP?.tombolAjukan == 1) {
                    btnAjukan.visibility = View.VISIBLE
                }

                if (dataDetailSP?.tombolBatalkan == 1) {
                    btnCancel.visibility = View.VISIBLE
                }

                if (dataDetailSP?.tombolTerima == 1) {
                    btnAccept.visibility = View.VISIBLE
                }

                if (dataDetailSP?.tombolTolak == 1) {
                    btnDecline.visibility = View.VISIBLE
                }
            }

            is AjukanSPResponse -> {
                toastNotify(response.message)
                btnAjukan.visibility = View.GONE
                btnCancel.visibility = View.GONE
            }

            is BatalkanSPResponse -> {
                toastNotify(response.message)
                btnAjukan.visibility = View.GONE
                btnCancel.visibility = View.GONE
            }

            is VerifikasiSPResponse -> {
                toastNotify(response.message)
                btnAccept.visibility = View.GONE
                btnDecline.visibility = View.GONE
            }
        }
    }

    private fun showDialogCatatan() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Berikan Catatan")

        var alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            this.layoutInflater.inflate(R.layout.dialog_catatan, null)

        dialogRootView.btnCatatan.setOnClickListener {

            val catatan = dialogRootView.etCatatanTolak.text.toString()
            disposable = suratPermintaanViewModel.verifikasi(idUser, idSp.toString(), "1", catatan)
                .subscribe(this::handleResponse, this::handleError)

            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

}
