package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityProfileBinding
import com.example.e_suratpermintaan.databinding.DialogChooseSignatureProfileBinding
import com.example.e_suratpermintaan.databinding.DialogSignaturePadProfileBinding
import com.example.e_suratpermintaan.databinding.ItemSimpleCheckboxBinding
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.framework.utils.FilePath
import com.example.e_suratpermintaan.framework.utils.Signature
import com.example.e_suratpermintaan.presentation.base.BaseActivity
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

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    companion object {
        const val FILE_REQUEST_CODE = 123
        const val PHOTO_SIGNATURE_REQUEST_CODE = 124
    }

    private val provileViewModel: ProfileViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private lateinit var progressDialog: ProgressDialog
    private var id: String? = null
    private var filePath: String? = null
    private var fileTtd: File? = null

    override fun getViewBinding(): ActivityProfileBinding =
        ActivityProfileBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = profilePreference.getProfile()?.id
        progressDialog = ProgressDialog(this)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initApiRequest() {
        disposable = provileViewModel.getProfile(id.toString())
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun setupListeners() {
        binding.btnSimpanProfile.setOnClickListener {
            progressDialog.setTitle("Memperbaharui Profile")
            progressDialog.setMessage("Mohon Tunggu...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val nama = binding.etNamaProfile.text.toString()
            val email = binding.etEmailProfile.text.toString()
            val deskripsi = binding.etDeskripsiProfile.text.toString()
            val passLama = binding.etPassLamaProfile.text.toString()
            val passBaru = binding.etPassBaruProfile.text.toString()
            val confirmPassBaru = binding.etConfirmPassBaruProfile.text.toString()

            if (passBaru != confirmPassBaru) {
                toastNotify("Password baru tidak sama")
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

                disposable = provileViewModel.editProfile(
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
            val selectFile = Intent(Intent.ACTION_PICK)
            selectFile.setType("image/*")
            startActivityForResult(selectFile, FILE_REQUEST_CODE)
        }

        binding.btnUploadTtd.setOnClickListener {
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

    private fun handleResponse(response: Any) {
        when (response) {
            is ProfileResponse -> {
                var dataProfile: DataProfile? = DataProfile()

                response.data?.forEach {
                    dataProfile = it
                }

                Glide.with(this).load(dataProfile?.fotoProfile).into(binding.imgFotoProfile)
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
                setResult(Activity.RESULT_OK, intent)

                filePath = null
                fileTtd = null
                progressDialog.dismiss()
                initApiRequest()
            }
        }
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)
        dismissSwipeRefreshLayout()
        progressDialog.dismiss()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            filePath = FilePath.getPath(this, fileUri as Uri)

            if (!filePath.isNullOrEmpty()) {
                toastNotify("Foto berhasil dipilih\nSilahkan menyimpan perubahan")
            }
        }

        if (requestCode == PHOTO_SIGNATURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            val ttdPath: String? = FilePath.getPath(this, fileUri as Uri)

            if (!ttdPath.isNullOrEmpty()) {
                toastNotify("Foto berhasil dipilih\nSilahkan menyimpan perubahan")
                fileTtd = File(ttdPath)
            } else {
                toastNotify("File Tdak ditemukan")
            }
        }
    }

    private fun showDialogMethodOption() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Unggah Tanda Tangan")

        val alertDialog = alertDialogBuilder.create()

        val dialogChooseSignatureProfileBinding =
            DialogChooseSignatureProfileBinding.inflate(LayoutInflater.from(this))

        dialogChooseSignatureProfileBinding.tvCreateSignatureProfile.setOnClickListener {
            showDialogSignaturePad()
            alertDialog.hide()
        }

        dialogChooseSignatureProfileBinding.tvGetSignatureProfile.setOnClickListener {
            val selectTtd = Intent(Intent.ACTION_PICK)
            selectTtd.type = "image/*"
            startActivityForResult(selectTtd, PHOTO_SIGNATURE_REQUEST_CODE)

            alertDialog.hide()
        }

        alertDialog.setView(dialogChooseSignatureProfileBinding.root)
        alertDialog.show()
    }

    private fun showDialogSignaturePad() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Unggah Tanda Tangan")

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
            fileTtd = Signature().saveSignature(this, ttdBitmap)
            if (fileTtd != null) {
                toastNotify("Tanda tangan telah dibuat\nSilahkan menyimpan perubahan")
            }
            alertDialog.hide()
        }

        dialogSignaturePadProfileBinding.btnClearTtdProfile.setOnClickListener {
            dialogSignaturePadProfileBinding.signaturePadProfile.clear()
        }

        alertDialog.setView(dialogSignaturePadProfileBinding.root)
        alertDialog.show()
    }
}
