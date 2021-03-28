package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityProfileBinding
import com.example.e_suratpermintaan.databinding.DialogChooseSignatureProfileBinding
import com.example.e_suratpermintaan.databinding.DialogSignaturePadProfileBinding
import com.example.e_suratpermintaan.databinding.ItemSimpleCheckboxBinding
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.STATUS_PROFILE_EDITED
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.framework.utils.FilePath
import com.example.e_suratpermintaan.framework.utils.Signature
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.dialog.AddImageOptionDialog
import com.example.e_suratpermintaan.presentation.dialog.ProgressBarDialog
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    companion object {
        private const val UPDATE_PROFILE_IMAGE_TAG = "UpdateProfileImage"
        private const val PROGRESS_BAR_DIALOG_TAG = "ProgressBarDialogProfile"
    }

    private val profileViewModel: ProfileViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private lateinit var addImageOptionDialog: AddImageOptionDialog
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var currentPhotoPath: String

    private var id: String? = null
    private var filePath: String? = null
    private var fileTtd: File? = null

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val fileUri = result.data?.data

                    Glide.with(this).load(fileUri).into(binding.imgFotoProfile)
                    filePath = FilePath.getPath(this, fileUri as Uri)

                    if (!filePath.isNullOrEmpty()) {
                        toastNotify(getString(R.string.success_select_photo_profile))
                    }
                }
            }
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                filePath = currentPhotoPath
                val fileUri = Uri.fromFile(File(filePath!!))
                Glide.with(this).load(fileUri).into(binding.imgFotoProfile)

                if (!filePath.isNullOrEmpty()) {
                    toastNotify(getString(R.string.success_select_photo_profile))
                }
            }
        }

    private val signatureResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fileUri = result.data?.data
                Glide.with(this).load(fileUri).into(binding.imgSignature)
                val ttdPath: String? = FilePath.getPath(this, fileUri as Uri)

                if (!ttdPath.isNullOrEmpty()) {
                    toastNotify(getString(R.string.success_make_signature))
                    fileTtd = File(ttdPath)
                } else {
                    toastNotify(getString(R.string.file_not_found))
                }
            }
        }


    override fun getViewBinding(): ActivityProfileBinding =
        ActivityProfileBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = profilePreference.getProfile()?.id
        progressBarDialog = ProgressBarDialog()
        addImageOptionDialog = AddImageOptionDialog()

        init()
    }

    private fun init() {
        setupTollbar()
        setupListeners()
        showSwipeRefreshLayout()
        initApiRequest()
    }

    private fun setupTollbar() {
        binding.toolbarProfile.text = getString(R.string.toolbar_profile)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    private fun initApiRequest() {
        disposable = profileViewModel.getProfile(id.toString())
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun setupListeners() {
        binding.btnSimpanProfile.setOnClickListener {
            showProgressBar()

            val nama = binding.etNamaProfile.text.toString()
            val email = binding.etEmailProfile.text.toString()
            val deskripsi = binding.etDeskripsiProfile.text.toString()
            val passLama = binding.etPassLamaProfile.text.toString()
            val passBaru = binding.etPassBaruProfile.text.toString()
            val confirmPassBaru = binding.etConfirmPassBaruProfile.text.toString()

            if (passBaru != confirmPassBaru) {
                toastNotify(getString(R.string.new_password_not_same))
            } else {
                val idUser = id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val namaUser = nama.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailUser = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val deskripsiUser = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
                val passLamaUser = passLama.toRequestBody("text/plain".toMediaTypeOrNull())
                val passBaruUser = passBaru.toRequestBody("text/plain".toMediaTypeOrNull())

                val partFile: MultipartBody.Part = if (!filePath.isNullOrEmpty()) {
                    val file = File(filePath!!)
                    val fileReqBody =
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("file", file.name, fileReqBody)
                } else {
                    val fileReqBody = "".toRequestBody(MultipartBody.FORM)
                    MultipartBody.Part.createFormData("file", "", fileReqBody)
                }

                val partTtd: MultipartBody.Part = if (fileTtd != null) {
                    val ttdReqBody =
                        fileTtd!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("ttd", fileTtd!!.name, ttdReqBody)
                } else {

                    val fileReqBody = "".toRequestBody(MultipartBody.FORM)
                    MultipartBody.Part.createFormData("ttd", "", fileReqBody)
                }

                disposable = profileViewModel.editProfile(
                    idUser,
                    emailUser,
                    passLamaUser,
                    passBaruUser,
                    namaUser,
                    deskripsiUser,
                    partFile,
                    partTtd
                )
                    .subscribe(this::handleResponse, this::handleError)

            }
        }

        binding.imgFotoProfile.setOnClickListener {
            showAddImageOptionDialog()
        }

        binding.imgSignature.setOnClickListener {
            showDialogMethodOption()
        }

        binding.passwordSeek.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etPassLamaProfile.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.etPassBaruProfile.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.etConfirmPassBaruProfile.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassLamaProfile.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.etPassBaruProfile.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.etConfirmPassBaruProfile.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this::initApiRequest)

    }

    private fun showSwipeRefreshLayout() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun dismissSwipeRefreshLayout() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun showAddImageOptionDialog() {
        addImageOptionDialog.onCameraButtonClick = {
            openCamera()
        }

        addImageOptionDialog.onGalleryButtonClick = {
            openGallery()
        }

        addImageOptionDialog.show(supportFragmentManager, UPDATE_PROFILE_IMAGE_TAG)
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager!!)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.e_suratpermintaan.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraResult.launch(takePictureIntent)
                }
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryResult.launch(galleryIntent)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun showDialogMethodOption() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.upload_signature))

        val alertDialog = alertDialogBuilder.create()

        val dialogChooseSignatureProfileBinding =
            DialogChooseSignatureProfileBinding.inflate(LayoutInflater.from(this))

        dialogChooseSignatureProfileBinding.tvCreateSignatureProfile.setOnClickListener {
            showDialogSignaturePad()
            alertDialog.hide()
        }

        dialogChooseSignatureProfileBinding.tvGetSignatureProfile.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            signatureResult.launch(galleryIntent)

            alertDialog.hide()
        }

        alertDialog.setView(dialogChooseSignatureProfileBinding.root)
        alertDialog.show()
    }

    private fun showDialogSignaturePad() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.upload_signature))

        val alertDialog = alertDialogBuilder.create()

        val dialogSignaturePadProfileBinding = DialogSignaturePadProfileBinding.inflate(
            LayoutInflater.from(this)
        )

        dialogSignaturePadProfileBinding.signaturePadProfile.setOnSignedListener(object :
            SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                dialogSignaturePadProfileBinding.btnSaveTtdProfile.isEnabled = false
                dialogSignaturePadProfileBinding.btnClearTtdProfile.isEnabled = false
            }

            override fun onSigned() {
                dialogSignaturePadProfileBinding.btnSaveTtdProfile.isEnabled = true
                dialogSignaturePadProfileBinding.btnClearTtdProfile.isEnabled = true
            }
        })

        dialogSignaturePadProfileBinding.btnSaveTtdProfile.setOnClickListener {
            val ttdBitmap =
                dialogSignaturePadProfileBinding.signaturePadProfile.transparentSignatureBitmap
            Glide.with(this).load(ttdBitmap).into(binding.imgSignature)
            fileTtd = Signature.saveSignature(this, ttdBitmap)
            if (fileTtd != null) {
                toastNotify(getString(R.string.success_make_signature))
            }
            alertDialog.hide()
        }

        dialogSignaturePadProfileBinding.btnClearTtdProfile.setOnClickListener {
            dialogSignaturePadProfileBinding.signaturePadProfile.clear()
        }

        alertDialog.setView(dialogSignaturePadProfileBinding.root)
        alertDialog.show()
    }

    private fun showProgressBar() {
        if (!progressBarDialog.isAdded) {
            progressBarDialog.show(supportFragmentManager, PROGRESS_BAR_DIALOG_TAG)
        }
    }

    private fun dismissProgressBar() {
        progressBarDialog.dismiss()
    }

    private fun handleResponse(response: Any) {
        when (response) {
            is ProfileResponse -> {
                val dataProfile: DataProfile? = response.data?.get(0)

                Glide.with(this).load(dataProfile?.fotoProfile).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_default_profile)
                        .error(R.drawable.ic_default_profile)
                ).into(binding.imgFotoProfile)

                Glide.with(this).load(dataProfile?.fotoTtd.toString()).diskCacheStrategy(
                    DiskCacheStrategy.NONE
                )
                    .skipMemoryCache(true).apply(
                        RequestOptions.placeholderOf(R.drawable.ic_select_image)
                            .error(R.drawable.ic_select_image)
                    ).into(binding.imgSignature)

                binding.etNamaProfile.setText(dataProfile?.name)
                binding.etEmailProfile.setText(dataProfile?.email)
                binding.tvRoleProfile.text = dataProfile?.namaRole
                binding.etDeskripsiProfile.setText(dataProfile?.desc)
                binding.tvUsernameProfile.text = dataProfile?.username

                binding.linearLayoutJenisProfile.removeAllViews()
                dataProfile?.jenis?.forEach {
                    val itemSimpleCheckboxBinding = ItemSimpleCheckboxBinding.inflate(
                        LayoutInflater.from(this),
                        binding.linearLayoutJenisProfile,
                        false
                    )
                    itemSimpleCheckboxBinding.checkbox.isChecked = true
                    itemSimpleCheckboxBinding.checkbox.text = it?.jenis
                    itemSimpleCheckboxBinding.checkbox.setOnCheckedChangeListener { _, checked ->
                        itemSimpleCheckboxBinding.checkbox.isChecked = !checked
                    }
                    binding.linearLayoutJenisProfile.addView(itemSimpleCheckboxBinding.root)
                }

                dismissSwipeRefreshLayout()
            }

            is EditProfileResponse -> {
                toastNotify(response.message)
                setResult(Activity.RESULT_OK, intent.putExtra("status", STATUS_PROFILE_EDITED))

                filePath = null
                fileTtd = null
                dismissProgressBar()
                initApiRequest()
            }
        }
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)
        dismissSwipeRefreshLayout()
        dismissProgressBar()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
