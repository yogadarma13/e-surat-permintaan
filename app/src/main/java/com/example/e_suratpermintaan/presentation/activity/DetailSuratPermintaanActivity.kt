package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.FILE_ITEM_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.FILE_ITEM_EDITED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.ITEM_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.ITEM_EDITED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.SP_AJUKAN
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.SP_BATAL
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.SP_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.SP_VERIFIKASI
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.*
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.LAUNCH_EDIT_ACTIVITY
import com.example.e_suratpermintaan.external.constants.IntentExtraConstants.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.framework.utils.Directory
import com.example.e_suratpermintaan.framework.utils.DownloadTask
import com.example.e_suratpermintaan.framework.utils.FileName
import com.example.e_suratpermintaan.framework.utils.Signature
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity.Companion.ID_SP_EDIT
import com.example.e_suratpermintaan.presentation.activity.HistorySuratPermintaanActivity.Companion.ID_SP_HISTORY
import com.example.e_suratpermintaan.presentation.activity.HistorySuratPermintaanActivity.Companion.JENIS_SP_HISTORY
import com.example.e_suratpermintaan.presentation.adapter.ItemSuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.FileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class DetailSuratPermintaanActivity : BaseActivity<ActivityDetailSuratPermintaanBinding>() {

    private val sharedMasterData: SharedMasterData by inject()
    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()

    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    private var kodeSp: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private lateinit var idUser: String
    private lateinit var idRole: String
    private lateinit var itemSuratPermintaanAdapter: ItemSuratPermintaanAdapter
    private lateinit var fileSuratPermintaanAdapter: BaseAdapter<FileSuratPermintaanViewHolder, ItemFileLampiranRowBinding>
    private lateinit var itemPrint: MenuItem
    private var jenisSP: String? = null
    private var alertDialog: AlertDialog? = null

    override fun getViewBinding(): ActivityDetailSuratPermintaanBinding =
        ActivityDetailSuratPermintaanBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_sp, menu)

        itemPrint = menu?.findItem(R.id.menuCetakSP)!!

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCetakSP -> {
                showDialogPrint()
            }

            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialogPrint() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme).setTitle("Pilihan cetak")

        alertDialog = alertDialogBuilder.create()

        val dialogPrintBinding = DialogPrintBinding.inflate(layoutInflater)

        dialogPrintBinding.tvPrintSP.setOnClickListener {
            val url = "https://jagat.jagatbuilding.co.id/master/surat_permintaan/print/${idSp}"
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }

        dialogPrintBinding.tvPrintByProses.setOnClickListener {
            val url =
                "https://jagat.jagatbuilding.co.id/master/surat_permintaan/print/${idSp}?id_user=${idUser}"
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }

        alertDialog?.setView(dialogPrintBinding.root)
        alertDialog?.show()
    }

    private fun init() {
        setupTollbar()
        setupItemSuratPermintaanRecyclerView()
        setupActionListeners()

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!isConnectedToInternet) {
                toastNotify("Please turn on the internet")
                stopRefresh()
                return@setOnRefreshListener
            }

            initApiRequest()
        }

        initApiRequest()
        startRefresh()
    }

    private fun setupTollbar() {
        binding.toolbarDetailSp.text = getString(R.string.toolbar_detail)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

        }
    }

    private fun initApiRequest() {
        sharedMasterData.getPersyaratanList().observe(this@DetailSuratPermintaanActivity, {
            it?.forEach { item ->
                // harus di uncheck untuk menghilangkan data "checked" untuk
                // data yang baru saja ditambahkan sebelum ini
                // Karna sharedViewModel berubah datanya karna diset "checked"
                item?.isChecked = false

                persyaratanList[item?.id.toString()] = item?.option.toString()
            }

            disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser)
                .subscribe(this::handleResponse, this::handleError)

            itemSuratPermintaanAdapter.itemList.clear()
            itemSuratPermintaanAdapter.notifyDataSetChanged()

            fileSuratPermintaanAdapter.itemList.clear()
            fileSuratPermintaanAdapter.notifyDataSetChanged()
        })
    }

    private fun setupItemSuratPermintaanRecyclerView() {

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@DetailSuratPermintaanActivity)
            adapter = itemSuratPermintaanAdapter
        }

        fileSuratPermintaanAdapter = BaseAdapter(
            ItemFileLampiranRowBinding::inflate,
            FileSuratPermintaanViewHolder::class.java
        )

        with(binding.recyclerViewFile) {
            layoutManager = LinearLayoutManager(this@DetailSuratPermintaanActivity)
            adapter = fileSuratPermintaanAdapter
        }
    }

    private fun setupActionListeners() {

        binding.btnHistory.setOnClickListener {
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

        binding.btnAjukan.setOnClickListener {
            showDialogPengajuan()
        }

        binding.btnCancel.setOnClickListener {
            alertDialog =
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Pembatalan")
                    .setMessage("Apa anda yakin ingin membatalkan permintaan ini?")
                    .setPositiveButton("Batalkan") { _, _ ->
                        binding.progressBarOverlay.root.visibility = View.VISIBLE
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        disposable = suratPermintaanViewModel.cancel(idUser, idSp.toString())
                            .subscribe(this::handleResponse, this::handleError)
                    }.setNegativeButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

            alertDialog?.show()
        }

        binding.btnAccept.setOnClickListener {
            showVerificationDialog()
        }

        binding.btnDecline.setOnClickListener {
            showDeclineDialog()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditSuratPermintaanActivity::class.java)
            intent.putExtra(ID_SP_EDIT, idSp)
            startActivityForResult(intent, LAUNCH_EDIT_ACTIVITY)
        }

        binding.btnDelete.setOnClickListener {
            alertDialog =
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Penghapusan")
                    .setMessage("Apa anda yakin ingin menghapus permintaan ini?")
                    .setPositiveButton("Ya, Hapus") { _, _ ->
                        binding.progressBarOverlay.root.visibility = View.VISIBLE
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        disposable = suratPermintaanViewModel.remove(idSp.toString())
                            .subscribe(this::handleResponse, this::handleError)
                    }.setNegativeButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

            alertDialog?.show()
        }
    }

    private fun startRefresh() {
        if (!isConnectedToInternet) return
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onSuratPermintaanDataChange(suratPermintaanDataChange: SuratPermintaanDataChange) {
        if (suratPermintaanDataChange.changeType == ITEM_EDITED ||
            suratPermintaanDataChange.changeType == ITEM_DELETED ||
            suratPermintaanDataChange.changeType == FILE_ITEM_EDITED ||
            suratPermintaanDataChange.changeType == FILE_ITEM_DELETED
        ) {
            initApiRequest()
            EventBus.getDefault().removeStickyEvent(suratPermintaanDataChange)
        }
    }

    private fun stopRefresh() {
        Handler().postDelayed({ binding.swipeRefreshLayout.isRefreshing = false }, 850)
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        stopRefresh()
        binding.progressBarOverlay.root.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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

                    binding.tvKodeDetail.text = kodeSp
                    binding.tvKodeSp.text = kodeSp
                    binding.tvNameProyekDetail.text = dataDetailSP?.namaProyek
                    binding.tvLocationDetail.text = dataDetailSP?.namaLokasi
                    binding.tvDateDetail.text = detailDate?.get(0)
                    binding.tvTimeDetail.text = detailDate?.get(1)
                    binding.tvStatusDetail.text = dataDetailSP?.statusPermintaan
                    binding.tvJenisDetail.text = dataDetailSP?.jenis
                    jenisSP = dataDetailSP?.jenis

                    val itemList: List<ItemsDetailSP?>? = dataDetailSP?.items
                    if (!itemList.isNullOrEmpty()) {
                        itemSuratPermintaanAdapter.persyaratanList.putAll(persyaratanList)
                        itemSuratPermintaanAdapter.idRole = idRole
                        binding.constraintTextItem.visibility = View.VISIBLE
                        itemList.forEach {
                            itemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
                            itemSuratPermintaanAdapter.viewType.add(dataDetailSP.jenis.toString())
                        }
                    } else {
                        binding.constraintTextItem.visibility = View.GONE
                    }
                    itemSuratPermintaanAdapter.notifyDataSetChanged()

                    val fileList = dataDetailSP?.fileLampiran
                    if (!fileList.isNullOrEmpty()) {
                        binding.constraintTextFile.visibility = View.VISIBLE
                        fileList.forEach {
                            fileSuratPermintaanAdapter.itemList.add(it as FileLampiranDetailSP)
                        }
                    } else {
                        binding.constraintTextFile.visibility = View.GONE
                    }

                    fileSuratPermintaanAdapter.notifyDataSetChanged()

                    if (dataDetailSP?.tombolAjukan == 1) {
                        binding.btnAjukan.visibility = View.VISIBLE
                    } else {
                        binding.btnAjukan.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolBatalkan == 1) {
                        binding.btnCancel.visibility = View.VISIBLE
                    } else {
                        binding.btnCancel.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolTerima == 1) {
                        binding.btnAccept.visibility = View.VISIBLE
                    } else {
                        binding.btnAccept.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolTolak == 1) {
                        binding.btnDecline.visibility = View.VISIBLE
                    } else {
                        binding.btnDecline.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolEdit == 1) {
                        binding.btnEdit.visibility = View.VISIBLE
                    } else {
                        binding.btnEdit.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolHapus == 1) {
                        binding.btnDelete.visibility = View.VISIBLE
                    } else {
                        binding.btnDelete.visibility = View.GONE
                    }

                    itemPrint.isVisible = dataDetailSP?.tombolCetak == 1

                }

            }

            is AjukanSPResponse -> {
                binding.progressBarOverlay.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                toastNotify(response.message)
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(SP_AJUKAN))
                initApiRequest()
            }

            is BatalkanSPResponse -> {
                binding.progressBarOverlay.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                toastNotify(response.message)
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(SP_BATAL))
                initApiRequest()
                Handler().postDelayed({
                    finish()
                }, 1000)
            }

            is VerifikasiSPResponse -> {
                binding.progressBarOverlay.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                toastNotify(response.message)
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(SP_VERIFIKASI))
                initApiRequest()
                Handler().postDelayed({
                    finish()
                }, 1000)
            }

            is DeleteSPResponse -> {
                binding.progressBarOverlay.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                toastNotify(response.message)
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(SP_DELETED))
                finish()
            }
        }
    }

    private fun showDeclineDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Berikan Catatan")

        alertDialog = alertDialogBuilder.create()

        val dialogCatatanBinding = DialogCatatanBinding.inflate(LayoutInflater.from(this))

        dialogCatatanBinding.btnCatatan.setOnClickListener {
            binding.progressBarOverlay.root.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            val catatan = dialogCatatanBinding.etCatatanTolak.text.toString()

            val userId = idUser.toRequestBody("text/plain".toMediaTypeOrNull())
            val spId = idSp.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val status = "1".toRequestBody("text/plain".toMediaTypeOrNull())
            val ctt = catatan.toRequestBody("text/plain".toMediaTypeOrNull())

            val fileReqBody = "".toRequestBody(MultipartBody.FORM)
            val filePart = MultipartBody.Part.createFormData("file", "", fileReqBody)

            disposable = suratPermintaanViewModel.verifikasi(userId, spId, status, ctt, filePart)
                .subscribe(this::handleResponse, this::handleError)

            alertDialog?.hide()
        }

        alertDialog?.setView(dialogCatatanBinding.root)
        alertDialog?.show()
    }

    private fun showDialogPengajuan() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Pengajuan Surat Permintaan")

        alertDialog = alertDialogBuilder.create()

        val dialogAjukanSuratBinding =
            DialogAjukanSuratBinding.inflate(LayoutInflater.from(this))

        dialogAjukanSuratBinding.signaturePadAjukan.setOnSignedListener(object :
            SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                dialogAjukanSuratBinding.btnClearTtdAjukan.isEnabled = false
            }

            override fun onSigned() {
                dialogAjukanSuratBinding.btnClearTtdAjukan.isEnabled = true
            }

        })

        dialogAjukanSuratBinding.btnClearTtdAjukan.setOnClickListener {
            dialogAjukanSuratBinding.signaturePadAjukan.clear()
        }

        dialogAjukanSuratBinding.btnFixAjukan.setOnClickListener {
            binding.progressBarOverlay.root.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            var fileTtd: File? = null
            val partTtd: MultipartBody.Part?
            val ttdBitmap: Bitmap?

            if (!dialogAjukanSuratBinding.signaturePadAjukan.isEmpty) {
                ttdBitmap = dialogAjukanSuratBinding.signaturePadAjukan.transparentSignatureBitmap
                fileTtd = Signature().saveSignature(this, ttdBitmap)
            }

            val userId = idUser.toRequestBody("text/plain".toMediaTypeOrNull())
            val spId = idSp.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            partTtd = if (fileTtd != null) {
                val fileReqBody =
                    fileTtd.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", fileTtd.name, fileReqBody)
            } else {
                val fileReqBody = "".toRequestBody(MultipartBody.FORM)
                MultipartBody.Part.createFormData("file", "", fileReqBody)
            }

            disposable = suratPermintaanViewModel.ajukan(userId, spId, partTtd)
                .subscribe(this::handleResponse, this::handleError)

            alertDialog?.hide()
        }

        dialogAjukanSuratBinding.btnCancelAjukan.setOnClickListener {
            alertDialog?.hide()
        }

        alertDialog?.setView(dialogAjukanSuratBinding.root)
        alertDialog?.show()
    }

    private fun showVerificationDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Verifikasi Surat Permintaan")

        alertDialog = alertDialogBuilder.create()

        val dialogVerifikasiSuratBinding =
            DialogVerifikasiSuratBinding.inflate(LayoutInflater.from(this))

        dialogVerifikasiSuratBinding.signaturePadVerif.setOnSignedListener(object :
            SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                dialogVerifikasiSuratBinding.btnClearTtdVerif.isEnabled = false
            }

            override fun onSigned() {
                dialogVerifikasiSuratBinding.btnClearTtdVerif.isEnabled = true
            }

        })

        dialogVerifikasiSuratBinding.btnClearTtdVerif.setOnClickListener {
            dialogVerifikasiSuratBinding.signaturePadVerif.clear()
        }

        dialogVerifikasiSuratBinding.btnFixVerif.setOnClickListener {
            binding.progressBarOverlay.root.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            var fileTtd: File? = null
            val ttdBitmap: Bitmap?

            if (!dialogVerifikasiSuratBinding.signaturePadVerif.isEmpty) {
                ttdBitmap =
                    dialogVerifikasiSuratBinding.signaturePadVerif.transparentSignatureBitmap
                fileTtd = Signature().saveSignature(this, ttdBitmap)
            }

            val userId = idUser.toRequestBody("text/plain".toMediaTypeOrNull())
            val spId = idSp.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val status = "0".toRequestBody("text/plain".toMediaTypeOrNull())
            val catatan = "".toRequestBody("text/plain".toMediaTypeOrNull())

            val partTtd: MultipartBody.Part = if (fileTtd != null) {
                val fileReqBody =
                    fileTtd.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", fileTtd.name, fileReqBody)
            } else {
                val fileReqBody = "".toRequestBody(MultipartBody.FORM)
                MultipartBody.Part.createFormData("file", "", fileReqBody)
            }

            disposable =
                suratPermintaanViewModel.verifikasi(userId, spId, status, catatan, partTtd)
                    .subscribe(this::handleResponse, this::handleError)

            alertDialog?.hide()
        }

        dialogVerifikasiSuratBinding.btnCancelVerif.setOnClickListener {
            alertDialog?.hide()
        }

        alertDialog?.setView(dialogVerifikasiSuratBinding.root)
        alertDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        alertDialog?.dismiss()
    }

}
