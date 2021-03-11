package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.FILE_ITEM_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.FILE_ITEM_EDITED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.ITEM_DELETED
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange.Companion.ITEM_EDITED
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.framework.utils.Directory
import com.example.e_suratpermintaan.framework.utils.DownloadTask
import com.example.e_suratpermintaan.framework.utils.FileName
import com.example.e_suratpermintaan.framework.utils.FilePath
import com.example.e_suratpermintaan.presentation.adapter.EditItemSuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.dialog.EditItemDialog
import com.example.e_suratpermintaan.presentation.dialog.PenugasanItemDialog
import com.example.e_suratpermintaan.presentation.dialog.TambahItemDialog
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditFileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.FileLampiranViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_edit_surat_permintaan.*
import kotlinx.android.synthetic.main.dialog_konfirmasi_simpan.view.*
import kotlinx.android.synthetic.main.dialog_tambah_file.view.*
import kotlinx.android.synthetic.main.dialog_tambah_file.view.etKeteranganFile
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditSuratPermintaanActivity : BaseActivity() {

    companion object {
        const val ID_SP_EDIT = "id_sp"
        // const val MASTER_PERSYARATAN = "master_persyaratan"
        const val PICKFILE_REQUEST_CODE = 999
    }

    private lateinit var alertDialogEdit: EditItemDialog
    private lateinit var alertDialogTambah: TambahItemDialog
    private lateinit var alertDialogPenugasan: PenugasanItemDialog

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val fileLampiranViewModel: FileLampiranViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val sharedMasterData: SharedMasterData by inject()

    private var dataDetailSP: DataDetailSP? = null
    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    // private var idFile: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private var idUser: String? = null
    private var idRole: String? = null
    private var filePath: String? = null

    private lateinit var editItemSuratPermintaanAdapter: EditItemSuratPermintaanAdapter
    private lateinit var editFileSuratPermintaanAdapter: BaseAdapter<EditFileSuratPermintaanViewHolder>

    override fun layoutId(): Int = R.layout.activity_edit_surat_permintaan

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
            TambahItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel)

        alertDialogEdit = EditItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel)

        alertDialogPenugasan =
            PenugasanItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel)

        setupTollbar()
        setupItemSuratPermintaanRecyclerView()
        setupActionListeners()
        initApiRequest()
    }

    private fun setupTollbar() {
        if (toolbar_edit_detail != null && toolbar != null) {
            toolbar_edit_detail.text = getString(R.string.toolbar_edit)
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayShowTitleEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
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
            sharedMasterData.getPersyaratanList().observe(this, Observer {
                it?.forEach { item ->

                    persyaratanList[item?.id.toString()] = item?.nama.toString()
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

        recyclerViewEditItem.layoutManager = LinearLayoutManager(this)
        recyclerViewEditItem.adapter = editItemSuratPermintaanAdapter

        editFileSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_file_lampiran_row,
            EditFileSuratPermintaanViewHolder::class.java
        )

        recyclerViewEditLampiran.layoutManager = LinearLayoutManager(this)
        recyclerViewEditLampiran.adapter = editFileSuratPermintaanAdapter
    }

    private fun setupActionListeners() {

        tvAddItem.setOnClickListener {
            alertDialogTambah.show()
        }

        tvAddLampiran.setOnClickListener {
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
                    processItemSuratPermintaan(idSp.toString(), data.id.toString(), idUser.toString())
                }
                EditItemSuratPermintaanViewHolder.BTN_UNPROCESS -> {
                    unProcessItemSuratPermintaan(idSp.toString(), data.id.toString(), idUser.toString())
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
                    val fileName = FileName.getFileNameFromURL(data.dir.toString())
                    if (Directory.checkDirectoryAndFileExists(this, fileName)) {
                        val downloadTask = DownloadTask(this, fileName)
                        downloadTask.execute(data.dir)
                    }
                }
            }
        }

        btnSimpanEdit.setOnClickListener {
            showDialogKonfirmasiSimpan()
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


    private fun processItemSuratPermintaan(idSp: String, idItem: String, idUser: String) {
        itemSuratPermintaanViewModel.processItem(idSp, idItem, idUser).subscribe(this::handleResponse, this::handleError)
    }

    private fun unProcessItemSuratPermintaan(idSp: String, idItem: String, idUser: String) {
        itemSuratPermintaanViewModel.unProcessItem(idSp, idItem, idUser).subscribe(this::handleResponse, this::handleError)
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
            is PenugasanItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is CreateItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()

                alertDialogTambah =
                    TambahItemDialog(this, sharedMasterData, itemSuratPermintaanViewModel)
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
                        tvAddItem.visibility = View.VISIBLE
                    } else {
                        tvAddItem.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolTambahFile == 1) {
                        tvAddLampiran.visibility = View.VISIBLE
                    } else {
                        tvAddLampiran.visibility = View.GONE
                    }

                    if (dataDetailSP?.tombolSimpan == 1) {
                        btnSimpanEdit.visibility = View.VISIBLE
                    } else {
                        btnSimpanEdit.visibility = View.GONE
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

        val dialogRootView =
            View.inflate(this, R.layout.dialog_konfirmasi_simpan, null)

        dialogRootView.btnSimpan.setOnClickListener {
            disposable = suratPermintaanViewModel.saveEdit(idSp.toString(), idUser.toString())
                .subscribe(this::handleResponse, this::handleError)
            alertDialog.hide()
        }

        dialogRootView.btnTutup.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    private fun showDialogTambahFile() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Tambah File")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_tambah_file, null)

        dialogRootView.btnPilihFile.setOnClickListener {
            val selectFile = Intent(Intent.ACTION_GET_CONTENT)
            selectFile.type = "application/pdf"
            startActivityForResult(selectFile, PICKFILE_REQUEST_CODE)
        }
        dialogRootView.btnTambahFile.setOnClickListener {
            if (filePath.equals("none") || filePath.isNullOrEmpty()) {
                toastNotify("File tidak ditemukan")
                return@setOnClickListener
            } else {
                try {
                    val file = File(filePath.toString())

                    val fileReqBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)

                    val partLampiran =
                        MultipartBody.Part.createFormData("file", file.name, fileReqBody)

                    val idSurat = RequestBody.create(MediaType.parse("text/plain"), idSp.toString())
                    val keteranganFile = RequestBody.create(
                        MediaType.parse("text/plain"),
                        dialogRootView.etKeteranganFile.text.toString()
                    )
                    disposable =
                        fileLampiranViewModel.addFile(idSurat, keteranganFile, partLampiran)
                            .subscribe(this::handleResponse, this::handleError)
                } catch (e: Exception) {
                    toastNotify("File tidak ditemukan")
                }
            }
            alertDialog.hide()
        }
        dialogRootView.btnBatalTambahFile.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    private fun showDialogEditFile(keterangan: String, id_file: String) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Edit File")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_tambah_file, null)

        dialogRootView.etKeteranganFile.setText(keterangan)
        dialogRootView.btnTambahFile.text = getString(R.string.btn_edit_file)

        dialogRootView.btnPilihFile.setOnClickListener {
            val selectFile = Intent(Intent.ACTION_GET_CONTENT)
            selectFile.type = "application/pdf"
            startActivityForResult(selectFile, PICKFILE_REQUEST_CODE)
        }
        dialogRootView.btnTambahFile.setOnClickListener {
            val idFile = RequestBody.create(MediaType.parse("text/plain"), id_file)
            val keteranganFile = RequestBody.create(
                MediaType.parse("text/plain"),
                dialogRootView.etKeteranganFile.text.toString()
            )
            val partLampiran: MultipartBody.Part

            if (filePath.equals("none")) {
                toastNotify("File tidak ditemukan")
                filePath = null
            } else {
                partLampiran = if (filePath.isNullOrEmpty()) {
                    val fileReqBody = RequestBody.create(MultipartBody.FORM, "")
                    MultipartBody.Part.createFormData("file", "", fileReqBody)
                } else {
                    val file = File(filePath.toString())
                    val fileReqBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    MultipartBody.Part.createFormData("file", file.name, fileReqBody)
                }

                disposable = fileLampiranViewModel.editFile(keteranganFile, partLampiran, idFile)
                    .subscribe(this::handleResponse, this::handleError)
            }

            alertDialog.hide()

        }
        dialogRootView.btnBatalTambahFile.setOnClickListener {
            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            filePath = FilePath.getPath(this, fileUri as Uri)

            if (filePath.isNullOrEmpty()) {
                filePath = "none"
            }
            toastNotify(filePath)

        }
    }
}
