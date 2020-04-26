package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.DetailSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DetailSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.adapter.ItemSuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_tambah_item.view.*
import kotlinx.android.synthetic.main.activity_detail_surat_permintaan.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailSuratPermintaanActivity : BaseActivity() {

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private var idSp: String? = null
    private var kodeSp: String? = null
    private lateinit var idUser: String
    private lateinit var itemSuratPermintaanAdapter: ItemSuratPermintaanAdapter

    override fun layoutId(): Int = R.layout.activity_detail_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString("id_sp")

        val profileId = profilePreference.getProfile()?.id

        if (profileId != null) {
            idUser = profileId
        }

        tvAddItem.setOnClickListener {
            startAddDialog()
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this@DetailSuratPermintaanActivity, HistorySuratPermintaanActivity::class.java)
            intent.putExtra("id_sp", idSp)
            startActivity(intent)
        }

        var detailSP = DetailSP(idSp.toString(), idUser)
        disposable = suratPermintaanViewModel.readDetail(detailSP)
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun handleResponse(response: Any) {
        when (response) {
            is CreateItemSPResponse -> {
                toastNotify(response.message)
            }
            is DetailSPResponse -> {

                val detailSPResponse = response.data
                val dataDetailSP = detailSPResponse?.get(0)
                kodeSp = dataDetailSP?.kode

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
                val itemArrayList: ArrayList<ItemsDetailSP?> = arrayListOf()

                itemList?.forEach {
                    itemArrayList.add(it)
                }

                itemSuratPermintaanAdapter = ItemSuratPermintaanAdapter(itemArrayList)
                itemSuratPermintaanAdapter.setOnClickListener(object :
                    ItemSuratPermintaanAdapter.OnClickItemListener {
                    override fun onClick(view: View, item: ItemsDetailSP?) {

                    }
                })

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = itemSuratPermintaanAdapter

            }
        }
    }

    private fun handleError(error: Throwable) {
        toastNotify(error.message)
    }

    private fun startAddDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Tambah Item")

        var alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            this.layoutInflater.inflate(R.layout.dialog_tambah_item, null)

        dialogRootView.btnTambah.setOnClickListener {
            val kodePekerjaan = dialogRootView.etKodePekerjaan.text.toString()
            val jenisBarang = dialogRootView.etJenisBarang.text.toString()
            val volume = dialogRootView.etVolume.text.toString()
            val satuan = dialogRootView.etSatuan.text.toString()
            val kapasitas = dialogRootView.etKapasistas.text.toString()
            val merk = dialogRootView.etMerk.text.toString()
            val waktuPemakaian = dialogRootView.etWaktuPemakaian.text.toString()

            alertDialog.hide()
            alertDialog = alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin menambah item?")
                .setPositiveButton("Ya") { _, _ ->
                    val createItemSP = CreateItemSP(
                        "S-AAA-1",
                        "010209",
                        "010209",
                        "M2",
                        "50",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "2020-04-20",
                        arrayListOf("1", "2"),
                        idUser
                    )
                    disposable = itemSuratPermintaanViewModel.addItem(createItemSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialog.hide()

                }.create()

            alertDialog.show()
        }


        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }
//
//    private fun startEditDialog() {
//
//        val alertDialogBuilder =
//            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
//                .setTitle("Ajukan Surat Permintaan")
//
//        var alertDialog = alertDialogBuilder.create()
//
//        val dialogRootView =
//            requireActivity().layoutInflater.inflate(R.layout.dialog_ajukan_sp, null)
//
//        val proyekAdapter =
//            ArrayAdapter(requireContext(), R.layout.material_spinner_item, proyekList)
//        val jenisAdapter = ArrayAdapter(requireContext(), R.layout.material_spinner_item, jenisList)
//
//        dialogRootView.spinnerProyek.setAdapter(proyekAdapter)
//        dialogRootView.spinnerJenis.setAdapter(jenisAdapter)
//        dialogRootView.btnAjukan.setOnClickListener {
//            val selectedProyek = dialogRootView.spinnerProyek.text.toString()
//            val selectedJenis = dialogRootView.spinnerJenis.text.toString()
//
//            idProyek = proyekList.find { it.nama == selectedProyek }.id.toString()
//            namaJenis = jenisList.find { it.nama == selectedJenis }.nama.toString()
//
//            alertDialog.hide()
//
//            alertDialog = alertDialogBuilder
//                .setMessage("Apakah Anda yakin ingin mengajukan?")
//                .setPositiveButton("Ya") { _, _ ->
//
//                    //                    val createSP = CreateSP(idProyek, namaJenis, idUser)
//                    disposable = suratPermintaanViewModel.add(idProyek, namaJenis, idUser)
//                        .subscribe(this::handleResponse, this::handleError)
//
//                    toastNotify("ID PROYEK : $idProyek \nNama Jenis : $namaJenis \nID USER : $idUser")
//
//                    alertDialog.hide()
//
//                }.create()
//
//            alertDialog.show()
//        }
//
//
//        alertDialog.setView(dialogRootView)
//        alertDialog.show()
//    }
}
