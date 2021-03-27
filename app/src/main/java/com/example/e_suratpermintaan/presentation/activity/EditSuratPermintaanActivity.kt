package com.example.e_suratpermintaan.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.FILE_ITEM_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.FILE_ITEM_EDITED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.ITEM_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.ITEM_EDITED
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.*
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.framework.utils.*
import com.example.e_suratpermintaan.presentation.adapter.EditItemSuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.dialog.DownloadProgressDialog
import com.example.e_suratpermintaan.presentation.dialog.EditItemDialog
import com.example.e_suratpermintaan.presentation.dialog.PenugasanItemDialog
import com.example.e_suratpermintaan.presentation.dialog.TambahItemDialog
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditFileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.FileLampiranViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditSuratPermintaanActivity : BaseActivity<ActivityEditSuratPermintaanBinding>() {

    companion object {
        const val ID_SP_EDIT = "id_sp"
        const val PICKFILE_REQUEST_CODE = 999
        private const val DOWNLOAD_PROGRESS_TAG = "DownloadProgress"
    }

    private lateinit var alertDialogEdit: EditItemDialog
    private lateinit var alertDialogTambah: TambahItemDialog
    private lateinit var alertDialogPenugasan: PenugasanItemDialog
    private lateinit var dialogTambahFileBinding: DialogTambahFileBinding

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val fileLampiranViewModel: FileLampiranViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val sharedMasterData: SharedMasterData by inject()

    private var dataDetailSP: DataDetailSP? = null
    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private var idUser: String? = null
    private var idRole: String? = null
    private var filePath: String? = null

    private lateinit var editItemSuratPermintaanAdapter: EditItemSuratPermintaanAdapter
    private lateinit var editFileSuratPermintaanAdapter: BaseAdapter<EditFileSuratPermintaanViewHolder, ItemFileLampiranRowBinding>

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                filePath = FilePath.getPath(this, uri)

                if (filePath.isNullOrEmpty()) {
                    filePath = "none"
                } else {
                    dialogTambahFileBinding.tvNamaFile.text =
                        filePath!!.substring(filePath!!.lastIndexOf('/') + 1)
                }
                toastNotify(filePath)
            }
        }

    override fun getViewBinding(): ActivityEditSuratPermintaanBinding =
        ActivityEditSuratPermintaanBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString(ID_SP_EDIT)

        editItemSuratPermintaanAdapter = EditItemSuratPermintaanAdapter()

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

    private fun init() {
        alertDialogTambah =
            TambahItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel, masterViewModel)

        alertDialogEdit =
            EditItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel, masterViewModel)

        alertDialogPenugasan =
            PenugasanItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel)

        setupTollbar()
        setupItemSuratPermintaanRecyclerView()
        setupActionListeners()
        initApiRequest()
    }

    private fun setupTollbar() {
        binding.toolbarEditDetail.text = getString(R.string.toolbar_edit)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initApiRequest() {
        if (idUser != null) {
            sharedMasterData.getPersyaratanList().observe(this, {
                it?.forEach { item ->

                    persyaratanList[item?.id.toString()] = item?.option.toString()
                }
                disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser.toString())
                    .subscribe(this::handleResponse, this::handleError)

                editItemSuratPermintaanAdapter.itemList.clear()
                editItemSuratPermintaanAdapter.notifyDataSetChanged()

                editFileSuratPermintaanAdapter.itemList.clear()
                editFileSuratPermintaanAdapter.notifyDataSetChanged()

                startRefresh()
            })
        }
    }

    private fun setupItemSuratPermintaanRecyclerView() {

        with(binding.recyclerViewEditItem) {
            layoutManager = LinearLayoutManager(this@EditSuratPermintaanActivity)
            adapter = editItemSuratPermintaanAdapter
        }

        editFileSuratPermintaanAdapter = BaseAdapter(
            ItemFileLampiranRowBinding::inflate,
            EditFileSuratPermintaanViewHolder::class.java
        )

        with(binding.recyclerViewEditLampiran) {
            layoutManager = LinearLayoutManager(this@EditSuratPermintaanActivity)
            adapter = editFileSuratPermintaanAdapter
        }
    }

    private fun setupActionListeners() {

        binding.tvAddItem.setOnClickListener {
            alertDialogTambah.show()
        }

        binding.tvAddLampiran.setOnClickListener {
            showDialogTambahFile()
        }

        editItemSuratPermintaanAdapter.setOnItemClickListener { item, actionString ->
            val data = item as ItemsDetailSP

            when (actionString) {
                BaseViewHolder.ROOTVIEW -> {
                    // Ignored
                }
                EditItemSuratPermintaanViewHolder.BTN_EDIT -> {
                    alertDialogEdit.show(data)
                }
                EditItemSuratPermintaanViewHolder.BTN_HAPUS -> {
                    val alertDialog =
                        AlertDialog.Builder(this)
                            .setTitle("Konfirmasi Hapus Item")
                            .setMessage("Apa anda yakin ingin menghapus item?")
                            .setPositiveButton("Hapus") { _, _ ->
                                disposable =
                                    itemSuratPermintaanViewModel.removeItem(data.id.toString())
                                        .subscribe(this::handleResponse, this::handleError)
                            }.setNegativeButton("Batal") { dialog, _ ->
                                dialog.dismiss()
                            }.create()

                    alertDialog.show()
                }
                EditItemSuratPermintaanViewHolder.BTN_PENUGASAN -> {
                    alertDialogPenugasan.show(data)
                }
                EditItemSuratPermintaanViewHolder.BTN_PROCESS -> {
                    processItemSuratPermintaan(
                        idSp.toString(),
                        data.id.toString(),
                        idUser.toString()
                    )
                }
                EditItemSuratPermintaanViewHolder.BTN_UNPROCESS -> {
                    unProcessItemSuratPermintaan(
                        idSp.toString(),
                        data.id.toString(),
                        idUser.toString()
                    )
                }
                EditItemSuratPermintaanViewHolder.BTN_REJECT -> {
                    rejectItemSuratPermintaan(
                        idUser.toString(),
                        idSp.toString(),
                        data.id.toString()
                    )
                }
                EditItemSuratPermintaanViewHolder.BTN_ROLLBACK -> {
                    rollbackItemSuratPermintaan(
                        idUser.toString(),
                        idSp.toString(),
                        data.id.toString()
                    )
                }
            }
        }


        editFileSuratPermintaanAdapter.setOnItemClickListener { item, actionString ->
            val data = item as FileLampiranDetailSP

            when (actionString) {
                BaseViewHolder.ROOTVIEW -> {
                    // Ignored
                }
                EditFileSuratPermintaanViewHolder.BTN_EDIT -> {
                    showDialogEditFile(data.keterangan.toString(), data.idFile.toString())
                }
                EditFileSuratPermintaanViewHolder.BTN_HAPUS -> {
                    val alertDialog =
                        AlertDialog.Builder(this)
                            .setTitle("Konfirmasi Hapus File")
                            .setMessage("Apa anda yakin ingin menghapus file?")
                            .setPositiveButton("Hapus") { _, _ ->
                                disposable =
                                    fileLampiranViewModel.removeFile(data.idFile.toString())
                                        .subscribe(this::handleResponse, this::handleError)
                            }.setNegativeButton("Batal") { dialog, _ ->
                                dialog.dismiss()
                            }.create()

                    alertDialog.show()
                }

                EditFileSuratPermintaanViewHolder.BTN_FILE -> {
                    val path = DownloadPath.getDownloadPath(this)
                    if (path != null) {
                        val downloadProgressDialog = DownloadProgressDialog()
                        downloadProgressDialog.show(
                            this.supportFragmentManager,
                            DOWNLOAD_PROGRESS_TAG
                        )
                        DownloadManager.download(
                            data.dir!!,
                            path,
                            object : DownloadManager.Callback {
                                override fun onDownloadStarted(totalLength: Long?) {

                                }

                                override fun onDownloadProgress(
                                    totalLength: Long?,
                                    downloadedLength: Long
                                ) {
                                    downloadProgressDialog.setProgress((downloadedLength * 100 / totalLength!!).toInt())
                                }

                                override fun onDownloadComplete(file: File) {
                                    downloadProgressDialog.dismiss()
                                    toastNotify("Download berhasil\nLokasi file di Penyimpanan/Download/E-Surat Permintaan")
                                }

                                override fun onDownloadError(e: Exception) {
                                    downloadProgressDialog.dismiss()
                                    toastNotify("Download gagal")
                                }
                            })
                    }
                }
            }
        }

        binding.btnSimpanEdit.setOnClickListener {
            showDialogKonfirmasiSimpan()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!isConnectedToInternet) {
                toastNotify("Please turn on the internet")
                stopRefresh()
                return@setOnRefreshListener
            }

            initApiRequest()
        }
    }

    private fun processItemSuratPermintaan(idSp: String, idItem: String, idUser: String) {
        itemSuratPermintaanViewModel.processItem(idSp, idItem, idUser)
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun unProcessItemSuratPermintaan(idSp: String, idItem: String, idUser: String) {
        itemSuratPermintaanViewModel.unProcessItem(idSp, idItem, idUser)
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun rejectItemSuratPermintaan(userId: String, spId: String, itemId: String) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(resources.getString(R.string.note_title))

        val alertDialog = alertDialogBuilder.create()

        val dialogCatatanBinding = DialogCatatanBinding.inflate(LayoutInflater.from(this))

        dialogCatatanBinding.btnCatatan.setOnClickListener {
            val note = dialogCatatanBinding.etCatatanTolak.text.toString()

            if (note.isNotEmpty()) {

                disposable = itemSuratPermintaanViewModel.rejectItem(userId, spId, itemId, note)
                    .subscribe(this::handleResponse, this::handleError)

                alertDialog.dismiss()
            } else {
                toastNotify(resources.getString(R.string.note_empty_message))
            }
        }

        alertDialog.setView(dialogCatatanBinding.root)
        alertDialog.show()
    }

    private fun rollbackItemSuratPermintaan(userId: String, spId: String, itemId: String) {
        itemSuratPermintaanViewModel.rollbackItem(userId, spId, itemId)
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun startRefresh() {
        if (!isConnectedToInternet) return
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun stopRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeRefreshLayout.isRefreshing = false
        }, 850)
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        stopRefresh()
    }

    fun handleResponse(response: Any) {
        stopRefresh()

        when (response) {
            is PenugasanItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is CreateItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()

                alertDialogTambah =
                    TambahItemDialog(
                        this,
                        sharedMasterData,
                        itemSuratPermintaanViewModel,
                        masterViewModel
                    )
                alertDialogTambah.initDialogViewTambah(dataProfile!!, dataDetailSP!!)

                EventBus.getDefault().postSticky(SuratPermintaanDataChange(ITEM_EDITED))
            }

            is EditItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(ITEM_EDITED))
            }

            is DeleteItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(ITEM_DELETED))
            }
            is ProcessItemSPResponse -> {
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(ITEM_EDITED))
            }

            is RejectItemSPResponse -> {
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(ITEM_EDITED))
            }

            is RollbackItemSPResponse -> {
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(ITEM_EDITED))
            }

            is CreateFileLampiranResponse -> {
                toastNotify(response.message)
                filePath = null
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(FILE_ITEM_EDITED))
            }

            is EditFileLampiranResponse -> {
                toastNotify(response.message)
                filePath = null
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(FILE_ITEM_EDITED))
            }

            is DeleteFileLampiranResponse -> {
                toastNotify(response.message)
                initApiRequest()
                EventBus.getDefault().postSticky(SuratPermintaanDataChange(FILE_ITEM_DELETED))
            }

            is DetailSPResponse -> {
                val detailSPResponse = response.data

                detailSPResponse.let {

                    dataDetailSP = detailSPResponse?.get(0)

                    if (dataDetailSP?.tombolTambahItem == 1) {
                        binding.tvAddItem.visibility = View.VISIBLE
                    } else {
                        binding.tvAddItem.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolTambahFile == 1) {
                        binding.tvAddLampiran.visibility = View.VISIBLE
                    } else {
                        binding.tvAddLampiran.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolSimpan == 1) {
                        binding.btnSimpanEdit.visibility = View.VISIBLE
                    } else {
                        binding.btnSimpanEdit.visibility = View.GONE
                    }

                    alertDialogTambah.initDialogViewTambah(dataProfile!!, dataDetailSP!!)
                    alertDialogEdit.initDialogViewEdit(dataProfile!!, dataDetailSP!!)
                    alertDialogPenugasan.initDialogViewPenugasan(dataProfile!!)

                    val itemList: List<ItemsDetailSP?>? = dataDetailSP!!.items

                    editItemSuratPermintaanAdapter.persyaratanList.putAll(persyaratanList)
                    editItemSuratPermintaanAdapter.idRole = idRole
                    itemList?.forEach {
                        editItemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
                        editItemSuratPermintaanAdapter.viewType.add(dataDetailSP!!.jenis.toString())
                    }

                    editItemSuratPermintaanAdapter.notifyDataSetChanged()

                    val lampiranList = dataDetailSP!!.fileLampiran

                    lampiranList?.forEach {
                        editFileSuratPermintaanAdapter.itemList.add(it as FileLampiranDetailSP)
                    }

                    editFileSuratPermintaanAdapter.notifyDataSetChanged()

                }
            }

            is SimpanEditSPResponse -> {
                toastNotify(response.message)
                finish()
            }

        }
    }

    private fun showDialogKonfirmasiSimpan() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Konfirmasi Simpan")

        val alertDialog = alertDialogBuilder.create()

        val dialogKonfirmasiSimpanBinding =
            DialogKonfirmasiSimpanBinding.inflate(LayoutInflater.from(this))

        dialogKonfirmasiSimpanBinding.btnSimpan.setOnClickListener {
            disposable = suratPermintaanViewModel.saveEdit(idSp.toString(), idUser.toString())
                .subscribe(this::handleResponse, this::handleError)
            alertDialog.hide()
        }

        dialogKonfirmasiSimpanBinding.btnTutup.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogKonfirmasiSimpanBinding.root)
        alertDialog.show()
    }

    private fun showDialogTambahFile() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Tambah File")

        val alertDialog = alertDialogBuilder.create()

        dialogTambahFileBinding = DialogTambahFileBinding.inflate(LayoutInflater.from(this))

        dialogTambahFileBinding.btnPilihFile.setOnClickListener {
            getContent.launch("application/pdf")
        }
        dialogTambahFileBinding.btnTambahFile.setOnClickListener {

            if (filePath.equals("none") || filePath.isNullOrEmpty()) {
                toastNotify("File tidak ditemukan")
                return@setOnClickListener
            } else {
                startRefresh()
                try {
                    val file = File(filePath.toString())

                    val fileReqBody =
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                    val partLampiran =
                        MultipartBody.Part.createFormData("file", file.name, fileReqBody)

                    val idSurat = idSp.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val keteranganFile = dialogTambahFileBinding.etKeteranganFile.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    disposable =
                        fileLampiranViewModel.addFile(idSurat, keteranganFile, partLampiran)
                            .subscribe(this::handleResponse, this::handleError)
                } catch (e: Exception) {
                    stopRefresh()
                    toastNotify("File tidak ditemukan")
                }
            }
            alertDialog.hide()
        }
        dialogTambahFileBinding.btnBatalTambahFile.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogTambahFileBinding.root)
        alertDialog.show()
    }

    private fun showDialogEditFile(keterangan: String, id_file: String) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.btn_edit_file))

        val alertDialog = alertDialogBuilder.create()

        dialogTambahFileBinding = DialogTambahFileBinding.inflate(LayoutInflater.from(this))

        dialogTambahFileBinding.etKeteranganFile.setText(keterangan)
        dialogTambahFileBinding.btnTambahFile.text = getString(R.string.btn_edit_file)

        dialogTambahFileBinding.btnPilihFile.setOnClickListener {
            getContent.launch("application/pdf")
        }
        dialogTambahFileBinding.btnTambahFile.setOnClickListener {
            val idFile = id_file.toRequestBody("text/plain".toMediaTypeOrNull())
            val keteranganFile = dialogTambahFileBinding.etKeteranganFile.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val partLampiran: MultipartBody.Part

            if (filePath.equals("none")) {
                toastNotify("File tidak ditemukan")
                filePath = null
            } else {
                startRefresh()
                partLampiran = if (filePath.isNullOrEmpty()) {
                    val fileReqBody = "".toRequestBody(MultipartBody.FORM)
                    MultipartBody.Part.createFormData("file", "", fileReqBody)
                } else {
                    val file = File(filePath.toString())
                    val fileReqBody =
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("file", file.name, fileReqBody)
                }

                disposable = fileLampiranViewModel.editFile(keteranganFile, partLampiran, idFile)
                    .subscribe(this::handleResponse, this::handleError)
            }

            alertDialog.hide()

        }
        dialogTambahFileBinding.btnBatalTambahFile.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogTambahFileBinding.root)
        alertDialog.show()
    }
}
