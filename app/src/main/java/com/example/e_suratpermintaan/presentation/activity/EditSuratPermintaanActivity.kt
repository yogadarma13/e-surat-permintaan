package com.example.e_suratpermintaan.presentation.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.dialog.EditItemDialog
import com.example.e_suratpermintaan.presentation.dialog.TambahItemDialog
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditFileSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.EditItemSuratPermintaanViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.FileLampiranViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_edit_surat_permintaan.*
import kotlinx.android.synthetic.main.activity_edit_surat_permintaan.swipeRefreshLayout
import okhttp3.MultipartBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditSuratPermintaanActivity : BaseActivity() {

//    companion object {
//        const val PICKFILE_REQUEST_CODE = 200;
//    }

    private lateinit var alertDialogEdit: EditItemDialog
    private lateinit var alertDialogTambah: TambahItemDialog

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel by viewModel()
    private val fileLampiranViewModel: FileLampiranViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private val sharedViewModel: SharedViewModel by inject()

    private var dataProfile: DataProfile? = null
    private var idSp: String? = null
    private var idFile: String? = null
    private lateinit var idUser: String
    private lateinit var filePart: MultipartBody.Part

    private lateinit var editItemSuratPermintaanAdapter: BaseAdapter<EditItemSuratPermintaanViewHolder>
    private lateinit var editFileSuratPermintaanAdapter: BaseAdapter<EditFileSuratPermintaanViewHolder>

    override fun layoutId(): Int = R.layout.activity_edit_surat_permintaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idSp = intent.extras?.getString("id_sp")

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
        editItemSuratPermintaanAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_item_row,
            EditItemSuratPermintaanViewHolder::class.java
        )

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

        tvAddIitem.setOnClickListener {
            alertDialogTambah.show()
        }

//        tvAddLampiran.setOnClickListener {
//            showDialogTambahFile()
//        }

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
            }
        }
        editFileSuratPermintaanAdapter.setOnItemClickListener { item, actionString ->
            val data = item as FileLampiranDetailSP

            when (actionString) {
                BaseViewHolder.ROOTVIEW -> {
                    // Ignored
                }
                EditFileSuratPermintaanViewHolder.BTN_EDIT -> {
//                    alertDialogEdit.show(data)
                }
                EditFileSuratPermintaanViewHolder.BTN_HAPUS -> {
                    val alertDialog =
                        AlertDialog.Builder(this)
                            .setTitle("Konfirmasi Hapus File")
                            .setMessage("Apa anda yakin ingin menghapus file?")
                            .setPositiveButton("Hapus") { _, _ ->
                                disposable =
                                    fileLampiranViewModel.removeFile(data.id_file.toString())
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

            is DeleteFileLampiranResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }

            is DetailSPResponse -> {
                val detailSPResponse = response.data

                detailSPResponse.let {

                    val dataDetailSP = detailSPResponse?.get(0)

                    alertDialogTambah.initDialogViewTambah(dataProfile!!, dataDetailSP!!)
                    alertDialogEdit.initDialogViewEdit(dataProfile!!, dataDetailSP)


                    val itemList: List<ItemsDetailSP?>? = dataDetailSP.items

                    itemList?.forEach {
                        editItemSuratPermintaanAdapter.itemList.add(it as ItemsDetailSP)
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

//        dialogRootView.btnPilihFile.setOnClickListener {
//            val selectFile = Intent(Intent.ACTION_GET_CONTENT)
//            selectFile.setType("*/*")
//            startActivityForResult(selectFile, PICKFILE_REQUEST_CODE)
//        }
//        dialogRootView.btnTambahFile.setOnClickListener {
//            val idSurat = RequestBody.create(MediaType.parse("text/plain"), idSp)
//            val keteranganFile = RequestBody.create(MediaType.parse("text/plain"), dialogRootView.etKeteranganFile.text.toString())
//            disposable = fileLampiranViewModel.addFile(idSurat, keteranganFile, filePart)
//                .subscribe(this::handleResponse, this::handleError)
//        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val fileUri = data?.data
//            val filePath = fileUri?.let { getPathFromUri(this, it) }
//            val file = File(filePath)
//
//            val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//
//            filePart = MultipartBody.Part.createFormData("file", file.name, fileReqBody)
//        }
//    }

//    fun getRealPath(context: Context, fileUri: Uri): String? {
//        val realPath: String?
//        // SDK < API11
//        realPath = if (Build.VERSION.SDK_INT < 11) {
//            getRealPathFromURI_BelowAPI11(context, fileUri)
//        } else if (Build.VERSION.SDK_INT < 19) {
//            getRealPathFromURI_API11to18(context, fileUri)
//        } else {
//            getRealPathFromURI_API19(context, fileUri)
//        }
//        return realPath
//    }
//
//    @SuppressLint("NewApi")
//    fun getRealPathFromURI_API11to18(context: Context, contentUri: Uri): String? {
//        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
//        var result: String? = null
//        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
//        val cursor: Cursor? = cursorLoader.loadInBackground()
//        if (cursor != null) {
//            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            result = cursor.getString(column_index)
//            cursor.close()
//        }
//        return result
//    }
//
//    fun getRealPathFromURI_BelowAPI11(context: Context, contentUri: Uri): String {
//        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
//        val cursor: Cursor? = context.contentResolver.query(contentUri, proj, null, null, null)
//        var column_index = 0
//        var result = ""
//        if (cursor != null) {
//            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            result = cursor.getString(column_index)
//            cursor.close()
//            return result
//        }
//        return result
//    }
//
//    /**
//     * Get a file path from a Uri. This will get the the path for Storage Access
//     * Framework Documents, as well as the _data field for the MediaStore and
//     * other file-based ContentProviders.
//     *
//     * @param context The context.
//     * @param uri     The Uri to query.
//     * @author paulburke
//     */
//    @SuppressLint("NewApi")
//    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
//        val isKitKat: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                val docId: String = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                if ("primary".equals(type, ignoreCase = true)) {
//                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//                }
//
//                // TODO handle non-primary volumes
//            } else if (isDownloadsDocument(uri)) {
//                val id: String = DocumentsContract.getDocumentId(uri)
//                val contentUri: Uri = ContentUris.withAppendedId(
//                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
//                )
//                return getDataColumn(context, contentUri, null, null)
//            } else if (isMediaDocument(uri)) {
//                val docId: String = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":").toTypedArray()
//                val type = split[0]
//                var contentUri: Uri? = null
//                if ("image" == type) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                } else if ("video" == type) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                } else if ("audio" == type) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                }
//                val selection = "_id=?"
//                val selectionArgs = arrayOf(
//                    split[1]
//                )
//                return getDataColumn(context, contentUri, selection, selectionArgs)
//            }
//        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//
//            // Return the remote address
//            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(context, uri, null, null)
//        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            return uri.path
//        }
//        return null
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    fun getDataColumn(
//        context: Context, uri: Uri?, selection: String?,
//        selectionArgs: Array<String>?
//    ): String? {
//        var cursor: Cursor? = null
//        val column = "_data"
//        val projection = arrayOf(
//            column
//        )
//        try {
//            cursor = context.getContentResolver().query(
//                uri!!, projection, selection, selectionArgs,
//                null
//            )
//            if (cursor != null && cursor.moveToFirst()) {
//                val index: Int = cursor.getColumnIndexOrThrow(column)
//                return cursor.getString(index)
//            }
//        } finally {
//            if (cursor != null) cursor.close()
//        }
//        return null
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    fun isExternalStorageDocument(uri: Uri): Boolean {
//        return "com.android.externalstorage.documents" == uri.authority
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    fun isDownloadsDocument(uri: Uri): Boolean {
//        return "com.android.providers.downloads.documents" == uri.authority
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    fun isMediaDocument(uri: Uri): Boolean {
//        return "com.android.providers.media.documents" == uri.authority
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is Google Photos.
//     */
//    fun isGooglePhotosUri(uri: Uri): Boolean {
//        return "com.google.android.apps.photos.content" == uri.authority
//    }

}
