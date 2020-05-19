package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.LAUNCH_EDIT_ACTIVITY
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.STATUS_SP_DELETED
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.STATUS_SP_EDITED
import com.example.e_suratpermintaan.external.constants.IntentExtraConstants.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.framework.utils.Directory
import com.example.e_suratpermintaan.framework.utils.DownloadTask
import com.example.e_suratpermintaan.framework.utils.FileName
import com.example.e_suratpermintaan.framework.utils.Signature
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity.Companion.ID_SP_EDIT
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity.Companion.MASTER_PERSYARATAN
import com.example.e_suratpermintaan.presentation.activity.HistorySuratPermintaanActivity.Companion.ID_SP_HISTORY
import com.example.e_suratpermintaan.presentation.activity.HistorySuratPermintaanActivity.Companion.JENIS_SP_HISTORY
import com.example.e_suratpermintaan.presentation.adapter.ItemSuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.FileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_detail_surat_permintaan.*
import kotlinx.android.synthetic.main.dialog_ajukan_surat.view.*
import kotlinx.android.synthetic.main.dialog_catatan.view.*
import kotlinx.android.synthetic.main.dialog_verifikasi_surat.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.Serializable

class DetailSuratPermintaanActivity : BaseActivity() {

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    private var kodeSp: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private lateinit var idUser: String
    private lateinit var idRole: String
    private lateinit var itemSuratPermintaanAdapter: ItemSuratPermintaanAdapter
    private lateinit var fileSuratPermintaanAdapter: BaseAdapter<FileSuratPermintaanViewHolder>
    private lateinit var itemPrint: MenuItem
    private var optionPrint = false
    private var optionEdit = false
    private var optionDelete = false
    private var jenisSP: String? = null

    override fun layoutId(): Int = R.layout.activity_detail_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (toolbar_detail_sp != null && toolbar != null) {
            toolbar_detail_sp.text = getString(R.string.toolbar_detail)
            setSupportActionBar(toolbar)
        }

        idSp = intent.extras?.getString(ID_SP_EXTRA_KEY)
        itemSuratPermintaanAdapter = ItemSuratPermintaanAdapter()

        idSp.let {
            dataProfile = profilePreference.getProfile()

            dataProfile.let {
                val profileId = it!!.id
                idRole = it.roleId.toString()
                if (profileId != null) {
                    idUser = profileId

                }

                init()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_EDIT_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                initApiRequest()

                val intent = Intent()
                intent.putExtra("status", STATUS_SP_EDITED)
                setResult(Activity.RESULT_OK, intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_sp, menu)

        itemPrint = menu?.findItem(R.id.menuCetakSP)!!

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuCetakSP -> {
                val url = "https://dev.karyastudio.com/e-spb/master/surat_permintaan/print/${idSp}"
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupItemSuratPermintaanRecyclerView()
        setupActionListeners()
        initApiRequest()
    }

    private fun initApiRequest() {
        disposable = masterViewModel.getPersyaratanList("all")
            .subscribe(this::handleResponse, this::handleError)

        disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser)
            .subscribe(this::handleResponse, this::handleError)

        itemSuratPermintaanAdapter.itemList.clear()
        itemSuratPermintaanAdapter.notifyDataSetChanged()

        fileSuratPermintaanAdapter.itemList.clear()
        fileSuratPermintaanAdapter.notifyDataSetChanged()
        startRefresh()
    }

    private fun setupItemSuratPermintaanRecyclerView() {
//        itemSuratPermintaanAdapter = BaseAdapter(
//            R.layout.item_surat_permintaan_item_row,
//            ItemSuratPermintaanViewHolder::class.java
//        )

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


//        three_dot.setOnClickListener {
//            showMenuItem(it)
//        }

        btnHistory.setOnClickListener {
            val intent = Intent(
                this@DetailSuratPermintaanActivity,
                HistorySuratPermintaanActivity::class.java
            )
            intent.putExtra(ID_SP_HISTORY, idSp)
            intent.putExtra(JENIS_SP_HISTORY, jenisSP)
            startActivity(intent)
        }

        fileSuratPermintaanAdapter.setOnItemClickListener { item, actionString ->
            val data = item as FileLampiranDetailSP

            when (actionString) {
                BaseViewHolder.ROOTVIEW -> {
                    // Ignored
                }

                FileSuratPermintaanViewHolder.BTN_FILE -> {
                    val fileName = FileName.getFileNameFromURL(data.dir.toString())
                    if (Directory.checkDirectoryAndFileExists(this, fileName)) {
                        val downloadTask = DownloadTask(this, fileName)
                        downloadTask.execute(data.dir)
                    }
                }
            }
        }

        btnAjukan.setOnClickListener {

            showDialogPengajuan()

//            val alertDialog =
//                AlertDialog.Builder(this)
//                    .setTitle("Konfirmasi Pengajuan")
//                    .setMessage("Apa anda yakin ingin mengajukan permintaan ini?")
//                    .setPositiveButton("Ajukan") { _, _ ->
//                        disposable = suratPermintaanViewModel.ajukan(idUser, idSp.toString())
//                            .subscribe(this::handleResponse, this::handleError)
//                    }.setNegativeButton("Tutup") { dialog, _ ->
//                        dialog.dismiss()
//                    }.create()
//
//            alertDialog.show()
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

            showDialogVerifikasi()

//            val alertDialog =
//                AlertDialog.Builder(this)
//                    .setTitle("Konfirmasi Penerimaan")
//                    .setMessage("Apa anda yakin ingin menerima permintaan ini?")
//                    .setPositiveButton("Terima") { _, _ ->
//                        disposable =
//                            suratPermintaanViewModel.verifikasi(idUser, idSp.toString(), "0", "")
//                                .subscribe(this::handleResponse, this::handleError)
//                    }.setNegativeButton("Tutup") { dialog, _ ->
//                        dialog.dismiss()
//                    }.create()
//
//            alertDialog.show()
        }

        btnDecline.setOnClickListener {
            showDialogCatatan()
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditSuratPermintaanActivity::class.java)
            intent.putExtra(ID_SP_EDIT, idSp)
            intent.putExtra(MASTER_PERSYARATAN, persyaratanList as Serializable)
            startActivityForResult(intent, LAUNCH_EDIT_ACTIVITY)
        }

        btnDelete.setOnClickListener {
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

    private fun handleResponse(response: Any) {
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
                        itemSuratPermintaanAdapter.persyaratanList.putAll(persyaratanList)
                        itemSuratPermintaanAdapter.idRole = idRole
                        constraint_text_item.visibility = View.VISIBLE
                        itemList.forEach {
                            itemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
                            itemSuratPermintaanAdapter.viewType.add(dataDetailSP.jenis.toString())
                        }
                    } else {
                        constraint_text_item.visibility = View.GONE
                    }
                    itemSuratPermintaanAdapter.notifyDataSetChanged()

                    val fileList = dataDetailSP?.fileLampiran
                    if (!fileList.isNullOrEmpty()) {
                        constraint_text_file.visibility = View.VISIBLE
                        fileList.forEach {
                            fileSuratPermintaanAdapter.itemList.add(it as FileLampiranDetailSP)
                        }
                    } else {
                        constraint_text_file.visibility = View.GONE
                    }

                    fileSuratPermintaanAdapter.notifyDataSetChanged()

//                    if (dataDetailSP?.tombolTambahItem == 1){
//                        tvAddItem.visibility = View.VISIBLE
//                    }

                    if (dataDetailSP?.tombolAjukan == 1) {
                        btnAjukan.visibility = View.VISIBLE
                    } else {
                        btnAjukan.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolBatalkan == 1) {
                        btnCancel.visibility = View.VISIBLE
                    } else {
                        btnCancel.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolTerima == 1) {
                        btnAccept.visibility = View.VISIBLE
                    } else {
                        btnAccept.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolTolak == 1) {
                        btnDecline.visibility = View.VISIBLE
                    } else {
                        btnDecline.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolEdit == 1) {
                        btnEdit.visibility = View.VISIBLE
                    } else {
                        btnEdit.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolHapus == 1) {
                        btnDelete.visibility = View.VISIBLE
                    } else {
                        btnDelete.visibility = View.GONE
                    }

                    itemPrint.isVisible = dataDetailSP?.tombolCetak == 1
//
//                    if (dataDetailSP?.tombolHapus == 1) {
//                        optionDelete = true
//                    }

                }

            }

            is AjukanSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
//                btnAjukan.visibility = View.GONE
//                btnCancel.visibility = View.GONE
//                btnEdit.visibility = View.GONE
//                btnAccept.visibility = View.GONE
//                btnDecline.visibility = View.GONE
            }

            is BatalkanSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
//                btnAjukan.visibility = View.GONE
//                btnCancel.visibility = View.GONE
//                btnEdit.visibility = View.GONE
//                btnAccept.visibility = View.GONE
//                btnDecline.visibility = View.GONE
            }

            is VerifikasiSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
//                btnAjukan.visibility = View.GONE
//                btnCancel.visibility = View.GONE
//                btnEdit.visibility = View.GONE
//                btnAccept.visibility = View.GONE
//                btnDecline.visibility = View.GONE
            }

            is DeleteSPResponse -> {
                toastNotify(response.message)
                val intent = Intent()
                intent.putExtra("status", STATUS_SP_DELETED)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            is MasterPersyaratanResponse -> {
                val dataPersyaratan = response.data

                dataPersyaratan?.forEach {
                    persyaratanList[it?.id.toString()] = it?.nama.toString()
                }
            }
        }
    }

//    private fun showMenuItem(v: View) {
//
//        PopupMenu(this, v).apply {
//
//            setOnMenuItemClickListener(this@DetailSuratPermintaanActivity)
//            inflate(R.menu.menu_item_sp)
//            if (optionPrint) menu.findItem(R.id.menuCetakSP).isVisible = true
//            if (optionEdit) menu.findItem(R.id.menuEditSP).isVisible = true
//            if (optionDelete) menu.findItem(R.id.menuHapusSP).isVisible = true
//
//            show()
//        }
//
//    }

//    override fun onMenuItemClick(item: MenuItem?): Boolean {
//        return when (item?.itemId) {
//            R.id.menuCetakSP -> {
//                val url = "https://dev.karyastudio.com/e-spb/master/surat_permintaan/print/${idSp}"
//                val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(i)
//
//                true
//            }
//            R.id.menuEditSP -> {
//                val intent = Intent(this, EditSuratPermintaanActivity::class.java)
//                intent.putExtra(ID_SP_EDIT, idSp)
//                intent.putExtra(MASTER_PERSYARATAN, persyaratanList as Serializable)
//                startActivityForResult(intent, LAUNCH_EDIT_ACTIVITY)
//                true
//            }
//            R.id.menuHapusSP -> {
//                val alertDialog =
//                    AlertDialog.Builder(this)
//                        .setTitle("Konfirmasi Penghapusan")
//                        .setMessage("Apa anda yakin ingin menghapus permintaan ini?")
//                        .setPositiveButton("Ya, Hapus") { _, _ ->
//                            disposable = suratPermintaanViewModel.remove(idSp.toString())
//                                .subscribe(this::handleResponse, this::handleError)
//                        }.setNegativeButton("Tutup") { dialog, _ ->
//                            dialog.dismiss()
//                        }.create()
//
//                alertDialog.show()
//                true
//            }
//            else -> false
//        }
//    }

    private fun showDialogCatatan() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Berikan Catatan")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_catatan, null)

        dialogRootView.btnCatatan.setOnClickListener {

            val catatan = dialogRootView.etCatatanTolak.text.toString()

            val userId = RequestBody.create(MediaType.parse("text/plain"), idUser)
            val spId = RequestBody.create(MediaType.parse("text/plain"), idSp.toString())
            val status = RequestBody.create(MediaType.parse("text/plain"), "1")
            val ctt = RequestBody.create(MediaType.parse("text/plain"), catatan)

            val fileReqBody = RequestBody.create(MultipartBody.FORM, "")
            val filePart = MultipartBody.Part.createFormData("file", "", fileReqBody)

            disposable = suratPermintaanViewModel.verifikasi(userId, spId, status, ctt, filePart)
                .subscribe(this::handleResponse, this::handleError)

            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    private fun showDialogPengajuan() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Pengajuan Surat Permintaan")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_ajukan_surat, null)

        dialogRootView.signaturePadAjukan.setOnSignedListener(object :
            SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                dialogRootView.btnClearTtdAjukan.isEnabled = false
            }

            override fun onSigned() {
                dialogRootView.btnClearTtdAjukan.isEnabled = true
            }

        })

        dialogRootView.btnClearTtdAjukan.setOnClickListener {
            dialogRootView.signaturePadAjukan.clear()
        }

        dialogRootView.btnFixAjukan.setOnClickListener {
            var fileTtd: File? = null
            val partTtd: MultipartBody.Part?
            val ttdBitmap: Bitmap?

            if (!dialogRootView.signaturePadAjukan.isEmpty) {
                ttdBitmap = dialogRootView.signaturePadAjukan.transparentSignatureBitmap
                fileTtd = Signature().saveSignature(this, ttdBitmap)
            }

            val userId = RequestBody.create(MediaType.parse("text/plain"), idUser)
            val spId = RequestBody.create(MediaType.parse("text/plain"), idSp.toString())

            partTtd = if (fileTtd != null) {
                val fileReqBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), fileTtd)
                MultipartBody.Part.createFormData("file", fileTtd.name, fileReqBody)
            } else {
                val fileReqBody = RequestBody.create(MultipartBody.FORM, "")
                MultipartBody.Part.createFormData("file", "", fileReqBody)
            }

            disposable = suratPermintaanViewModel.ajukan(userId, spId, partTtd)
                .subscribe(this::handleResponse, this::handleError)

            alertDialog.hide()
        }

        dialogRootView.btnCancelAjukan.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    private fun showDialogVerifikasi() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Verifikasi Surat Permintaan")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_verifikasi_surat, null)

        dialogRootView.signaturePadVerif.setOnSignedListener(object :
            SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                dialogRootView.btnClearTtdVerif.isEnabled = false
            }

            override fun onSigned() {
                dialogRootView.btnClearTtdVerif.isEnabled = true
            }

        })

        dialogRootView.btnClearTtdVerif.setOnClickListener {
            dialogRootView.signaturePadVerif.clear()
        }

        dialogRootView.btnFixVerif.setOnClickListener {
            var fileTtd: File? = null
            val partTtd: MultipartBody.Part?
            val ttdBitmap: Bitmap?

            if (!dialogRootView.signaturePadVerif.isEmpty) {
                ttdBitmap = dialogRootView.signaturePadVerif.transparentSignatureBitmap
                fileTtd = Signature().saveSignature(this, ttdBitmap)
            }

            val userId = RequestBody.create(MediaType.parse("text/plain"), idUser)
            val spId = RequestBody.create(MediaType.parse("text/plain"), idSp.toString())
            val status = RequestBody.create(MediaType.parse("text/plain"), "0")
            val catatan = RequestBody.create(MediaType.parse("text/plain"), "")

            partTtd = if (fileTtd != null) {
                val fileReqBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), fileTtd)
                MultipartBody.Part.createFormData("file", fileTtd.name, fileReqBody)
            } else {
                val fileReqBody = RequestBody.create(MultipartBody.FORM, "")
                MultipartBody.Part.createFormData("file", "", fileReqBody)
            }

            disposable =
                suratPermintaanViewModel.verifikasi(userId, spId, status, catatan, partTtd)
                    .subscribe(this::handleResponse, this::handleError)

            alertDialog.hide()
        }

        dialogRootView.btnCancelVerif.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

}
