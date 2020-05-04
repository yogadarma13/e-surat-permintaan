package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.FilePath
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.adapter.EditItemSuratPermintaanAdapter
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.dialog.EditItemDialog
import com.example.e_suratpermintaan.presentation.dialog.TambahItemDialog
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditFileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_edit_surat_permintaan.*
import kotlinx.android.synthetic.main.activity_edit_surat_permintaan.swipeRefreshLayout
import kotlinx.android.synthetic.main.dialog_tambah_file.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditSuratPermintaanActivity : BaseActivity() {

    companion object {
        const val ID_SP_EDIT = "id_sp";
        const val MASTER_PERSYARATAN ="master_persyaratan"
        const val PICKFILE_REQUEST_CODE = 999
    }

    private lateinit var alertDialogEdit: EditItemDialog
    private lateinit var alertDialogTambah: TambahItemDialog

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val fileLampiranViewModel: FileLampiranViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val sharedViewModel: SharedViewModel by inject()

    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    private var idFile: String? = null
    private var persyaratanList = mutableMapOf<String, String>()
    private lateinit var idUser: String
    private lateinit var partLampiran: MultipartBody.Part

    private lateinit var editItemSuratPermintaanAdapter: EditItemSuratPermintaanAdapter
    private lateinit var editFileSuratPermintaanAdapter: BaseAdapter<EditFileSuratPermintaanViewHolder>

    override fun layoutId(): Int = R.layout.activity_edit_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString(ID_SP_EDIT)
        persyaratanList = intent.getSerializableExtra(MASTER_PERSYARATAN) as MutableMap<String, String>

        editItemSuratPermintaanAdapter = EditItemSuratPermintaanAdapter()

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
        alertDialogTambah =
            TambahItemDialog(this, sharedViewModel, itemSuratPermintaanViewModel)

        alertDialogEdit = EditItemDialog(this, sharedViewModel, itemSuratPermintaanViewModel)

        setupItemSuratPermintaanRecyclerView()
        setupActionListeners()
        initApiRequest()
    }

    private fun initApiRequest() {

        disposable = suratPermintaanViewModel.readDetail(idSp.toString(), idUser)
            .subscribe(this::handleResponse, this::handleError)

        editItemSuratPermintaanAdapter.itemList.clear()
        editItemSuratPermintaanAdapter.notifyDataSetChanged()

        editFileSuratPermintaanAdapter.itemList.clear()
        editFileSuratPermintaanAdapter.notifyDataSetChanged()

        startRefresh()
    }

    private fun setupItemSuratPermintaanRecyclerView(){

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

//        editItemSuratPermintaanAdapter.setOnItemClickListener { item, actionString ->
//            val data = item as ItemsDetailSP
//
//            when (actionString) {
//                BaseViewHolder.ROOTVIEW -> {
//                    // Ignored
//                }
//                EditItemSuratPermintaanViewHolder.BTN_EDIT -> {
//                    alertDialogEdit.show(data)
//                }
//                EditItemSuratPermintaanViewHolder.BTN_HAPUS -> {
//                    val alertDialog =
//                        AlertDialog.Builder(this)
//                            .setTitle("Konfirmasi Hapus Item")
//                            .setMessage("Apa anda yakin ingin menghapus item?")
//                            .setPositiveButton("Hapus") { _, _ ->
//                                disposable =
//                                    itemSuratPermintaanViewModel.removeItem(data.id.toString())
//                                        .subscribe(this::handleResponse, this::handleError)
//                            }.setNegativeButton("Batal") { dialog, _ ->
//                                dialog.dismiss()
//                            }.create()
//
//                    alertDialog.show()
//                }
//            }
//        }
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
                    toastNotify(data.dir)
                }
            }
        }

        btnSimpanEdit.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
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

        when(response) {
            is CreateItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is EditItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is DeleteItemSPResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is CreateFileLampiranResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is EditFileLampiranResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is DeleteFileLampiranResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is DetailSPResponse -> {
                val detailSPResponse = response.data

                detailSPResponse.let {

                    val dataDetailSP = detailSPResponse?.get(0)

                    if (dataDetailSP?.tombolTambahItem == 1) {
                        tvAddItem.visibility = View.VISIBLE
                    }

                    if (dataDetailSP?.tombolTambahFile == 1) {
                        tvAddLampiran.visibility = View.VISIBLE
                    }

                    if (dataDetailSP?.tombolSimpan == 1) {
                        btnSimpanEdit.visibility = View.VISIBLE
                    }

                    alertDialogTambah.initDialogViewTambah(dataProfile!!, dataDetailSP!!)
                    alertDialogEdit.initDialogViewEdit(dataProfile!!, dataDetailSP)


                    val itemList: List<ItemsDetailSP?>? = dataDetailSP.items

                    editItemSuratPermintaanAdapter.persyaratanList.putAll(persyaratanList)
                    itemList?.forEach {
                        editItemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
                        editItemSuratPermintaanAdapter.viewType.add(dataDetailSP.jenis.toString())
                    }

                    editItemSuratPermintaanAdapter.notifyDataSetChanged()



                    val lampiranList = dataDetailSP.fileLampiran

                    lampiranList?.forEach {
                        editFileSuratPermintaanAdapter.itemList.add(it as FileLampiranDetailSP)
                    }

                    editFileSuratPermintaanAdapter.notifyDataSetChanged()

                }
            }

        }
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
            selectFile.setType("application/pdf")
            startActivityForResult(selectFile, PICKFILE_REQUEST_CODE)
        }
        dialogRootView.btnTambahFile.setOnClickListener {
            if (partLampiran == null) {
                toastNotify("File belum dipilih")
            }
            else {
                val idSurat = RequestBody.create(MediaType.parse("text/plain"), idSp)
                val keteranganFile = RequestBody.create(MediaType.parse("text/plain"), dialogRootView.etKeteranganFile.text.toString())
                disposable = fileLampiranViewModel.addFile(idSurat, keteranganFile, partLampiran)
                    .subscribe(this::handleResponse, this::handleError)

                alertDialog.hide()
            }
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    private fun showDialogEditFile(keterangan: String, id_file: String) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Tambah File")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_tambah_file, null)

        dialogRootView.etKeteranganFile.setText(keterangan)
        dialogRootView.btnTambahFile.text = "Edit File"

        dialogRootView.btnPilihFile.setOnClickListener {
            val selectFile = Intent(Intent.ACTION_GET_CONTENT)
            selectFile.setType("application/pdf")
            startActivityForResult(selectFile, PICKFILE_REQUEST_CODE)
        }
        dialogRootView.btnTambahFile.setOnClickListener {
            if (partLampiran == null) {
                toastNotify("File belum dipilih")
            }
            else {
                val idFile = RequestBody.create(MediaType.parse("text/plain"), id_file)
                val keteranganFile = RequestBody.create(MediaType.parse("text/plain"), dialogRootView.etKeteranganFile.text.toString())
                disposable = fileLampiranViewModel.editFile(keteranganFile, partLampiran, idFile)
                    .subscribe(this::handleResponse, this::handleError)

                alertDialog.hide()
            }

        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            val filePath = FilePath.getPath(this, fileUri as Uri)

            toastNotify(filePath)

            val file = File(filePath)

            val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

            partLampiran = MultipartBody.Part.createFormData("file", file.name, fileReqBody)
        }
    }
}
