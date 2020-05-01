package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.FileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.ItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_detail_surat_permintaan.*
import kotlinx.android.synthetic.main.dialog_catatan.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailSuratPermintaanActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {

    companion object {
        const val ID_SP_EXTRA_KEY = "id_sp"
        const val STATUS_SP_DELETED = "SP_DELETED"
    }

//    private lateinit var alertDialogEdit: EditItemDialog
//    private lateinit var alertDialogTambah: TambahItemDialog

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    private var kodeSp: String? = null
    private lateinit var idUser: String
    private lateinit var itemSuratPermintaanAdapter: BaseAdapter<ItemSuratPermintaanViewHolder>
    private lateinit var fileSuratPermintaanAdapter: BaseAdapter<FileSuratPermintaanViewHolder>
    private var optionPrint = false
    private var optionEdit = false
    private var optionDelete = false
    private var jenisSP: String? = null

    override fun layoutId(): Int = R.layout.activity_detail_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString(ID_SP_EXTRA_KEY)

        idSp.let {
            dataProfile = profilePreference.getProfile()

            dataProfile.let {
                val profileId = it!!.id
                if (profileId != null) {
                    idUser = profileId
                }

                init()
            }
        }

    }

    private fun init() {
        setupItemSuratPermintaanRecyclerView()
        setupActionListeners()
        initApiRequest()
    }

    private fun initApiRequest() {
        disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser)
            .subscribe(this::handleResponse, this::handleError)

        itemSuratPermintaanAdapter.itemList.clear()
        itemSuratPermintaanAdapter.notifyDataSetChanged()
        startRefresh()
    }

    private fun setupItemSuratPermintaanRecyclerView() {
        itemSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_item_row,
            ItemSuratPermintaanViewHolder::class.java
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemSuratPermintaanAdapter

        fileSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_file_lampiran_row,
            FileSuratPermintaanViewHolder::class.java
        )

        recyclerViewFile.layoutManager = LinearLayoutManager(this)
        recyclerViewFile.adapter = fileSuratPermintaanAdapter
    }

    private fun setupActionListeners() {


        three_dot.setOnClickListener {
            showMenuItem(it)
        }

        btnHistory.setOnClickListener {
            val intent = Intent(
                this@DetailSuratPermintaanActivity,
                HistorySuratPermintaanActivity::class.java
            )
            intent.putExtra("id_sp", idSp)
            intent.putExtra("jenis_sp", jenisSP)
            startActivity(intent)
        }

        btnAjukan.setOnClickListener {

            val alertDialog =
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Pengajuan")
                    .setMessage("Apa anda yakin ingin mengajukan permintaan ini?")
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
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Pembatalan")
                    .setMessage("Apa anda yakin ingin membatalkan permintaan ini?")
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
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Penerimaan")
                    .setMessage("Apa anda yakin ingin menerima permintaan ini?")
                    .setPositiveButton("Terima") { _, _ ->
                        disposable =
                            suratPermintaanViewModel.verifikasi(idUser, idSp.toString(), "0", "")
                                .subscribe(this::handleResponse, this::handleError)
                    }.setNegativeButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

            alertDialog.show()
        }

        btnDecline.setOnClickListener {
            showDialogCatatan()
        }

        swipeRefreshLayout.setOnRefreshListener {
            if (!isConnectedToInternet) {
                toastNotify("Please turn on the internet")
                stopRefresh()
                return@setOnRefreshListener
            }

            initApiRequest()
        }
    }

    private fun startRefresh() {
        if (!isConnectedToInternet) return
        swipeRefreshLayout.isRefreshing = true
    }

    private fun stopRefresh() {
        Handler().postDelayed({ swipeRefreshLayout.isRefreshing = false }, 850)
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        stopRefresh()
    }

    fun handleResponse(response: Any) {
        stopRefresh()

        when (response) {

            is DetailSPResponse -> {

                val detailSPResponse = response.data
                detailSPResponse.let {

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
                    jenisSP = dataDetailSP?.jenis

                    val itemList: List<ItemsDetailSP?>? = dataDetailSP?.items
                    if (!itemList.isNullOrEmpty()) {
                        constraint_text_item.visibility = View.VISIBLE
                        itemList.forEach {
                            itemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
                        }
                    }
                    itemSuratPermintaanAdapter.notifyDataSetChanged()

                    val fileList = dataDetailSP?.fileLampiran
                    if (!fileList.isNullOrEmpty()) {
                        constraint_text_file.visibility = View.VISIBLE
                        fileList.forEach {
                            fileSuratPermintaanAdapter.itemList.add(it as FileLampiranDetailSP)
                        }
                    }

                    fileSuratPermintaanAdapter.notifyDataSetChanged()

//                    if (dataDetailSP?.tombolTambahItem == 1){
//                        tvAddItem.visibility = View.VISIBLE
//                    }

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

                    if (dataDetailSP?.tombolCetak == 1) {
                        optionPrint = true
                    }

                    if (dataDetailSP?.tombolEdit == 1) {
                        optionEdit = true
                    }

                    if (dataDetailSP?.tombolHapus == 1) {
                        optionDelete = true
                    }

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

            is DeleteSPResponse -> {
                toastNotify(response.message)
                val intent = Intent()
                intent.putExtra("status", STATUS_SP_DELETED)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun showMenuItem(v: View) {

        PopupMenu(this, v).apply {

            setOnMenuItemClickListener(this@DetailSuratPermintaanActivity)
            inflate(R.menu.menu_item_sp)
            if (optionPrint) menu.findItem(R.id.menuCetakSP).isVisible = true
            if (optionEdit) menu.findItem(R.id.menuEditSP).isVisible = true
            if (optionDelete) menu.findItem(R.id.menuHapusSP).isVisible = true

            show()
        }

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menuCetakSP -> {
                val url = "https://dev.karyastudio.com/e-spb/master/surat_permintaan/print/${idSp}"
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(i)

                true
            }
            R.id.menuEditSP -> {
                val intent = Intent(this, EditSuratPermintaanActivity::class.java)
                intent.putExtra("id_sp", idSp)
                startActivity(intent)
                true
            }
            R.id.menuHapusSP -> {
                val alertDialog =
                    AlertDialog.Builder(this)
                        .setTitle("Konfirmasi Penghapusan")
                        .setMessage("Apa anda yakin ingin menghapus permintaan ini?")
                        .setPositiveButton("Ya, Hapus") { _, _ ->
                            disposable = suratPermintaanViewModel.remove(idSp.toString())
                                .subscribe(this::handleResponse, this::handleError)
                        }.setNegativeButton("Tutup") { dialog, _ ->
                            dialog.dismiss()
                        }.create()

                alertDialog.show()
                true
            }
            else -> false
        }
    }

    private fun showDialogCatatan() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Berikan Catatan")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_catatan, null)

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
