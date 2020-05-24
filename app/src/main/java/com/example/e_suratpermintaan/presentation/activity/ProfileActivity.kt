package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.utils.FilePath
import com.example.e_suratpermintaan.framework.utils.Signature
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_choose_signature_profile.view.*
import kotlinx.android.synthetic.main.dialog_signature_pad_profile.view.*
import kotlinx.android.synthetic.main.item_simple_checkbox.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class ProfileActivity : BaseActivity() {

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

    override fun layoutId(): Int = R.layout.activity_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = profilePreference.getProfile()?.id
        progressDialog = ProgressDialog(this)

        init()

    }

    private fun init() {
        setupTollbar()
        setupListeners()
        initApiRequest()
    }

    private fun setupTollbar() {
        if (toolbar_profile != null && toolbar != null) {
            toolbar_profile.text = getString(R.string.toolbar_profile)
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.setDisplayShowTitleEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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
        btnSimpanProfile.setOnClickListener {
            progressDialog.setTitle("Memperbaharui Profile")
            progressDialog.setMessage("Mohon Tunggu...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            val nama = etNamaProfile.text.toString()
            val email = etEmailProfile.text.toString()
            val deskripsi = etDeskripsiProfile.text.toString()
            val passLama = etPassLamaProfile.text.toString()
            val passBaru = etPassBaruProfile.text.toString()
            val confirmPassBaru = etConfirmPassBaruProfile.text.toString()

            if (!passBaru.equals(confirmPassBaru)) {
                toastNotify("Password baru tidak sama")
            } else {
                val idUser = RequestBody.create(MediaType.parse("text/plain"), id.toString())
                val namaUser = RequestBody.create(MediaType.parse("text/plain"), nama)
                val emailUser = RequestBody.create(MediaType.parse("text/plain"), email)
                val deskripsiUser = RequestBody.create(MediaType.parse("text/plain"), deskripsi)
                val passLamaUser = RequestBody.create(MediaType.parse("text/plain"), passLama)
                val passBaruUser = RequestBody.create(MediaType.parse("text/plain"), passBaru)
                var partFile: MultipartBody.Part?
                var partTtd: MultipartBody.Part?

                if (!filePath.isNullOrEmpty()) {
                    val file = File(filePath)
                    val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    partFile = MultipartBody.Part.createFormData("file", file.name, fileReqBody)
                } else {
                    val fileReqBody = RequestBody.create(MultipartBody.FORM, "")
                    partFile = MultipartBody.Part.createFormData("file", "", fileReqBody)
                }

                partTtd = if (fileTtd != null) {
                    val ttdReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileTtd)
                    MultipartBody.Part.createFormData("ttd", fileTtd!!.name, ttdReqBody)
                } else {
                    val fileReqBody = RequestBody.create(MultipartBody.FORM, "")
                    MultipartBody.Part.createFormData("ttd", "", fileReqBody)
                }

                disposable = provileViewModel.editProfile(idUser, emailUser, passLamaUser, passBaruUser, namaUser, deskripsiUser, partFile, partTtd)
                    .subscribe(this::handleResponse, this::handleError)

            }
        }

        imgFotoProfile.setOnClickListener {
            val selectFile = Intent(Intent.ACTION_PICK)
            selectFile.setType("image/*")
            startActivityForResult(selectFile, FILE_REQUEST_CODE)
        }

        btnUploadTtd.setOnClickListener {
            showDialogMethodOption()
        }

    }


    private fun handleResponse(response: Any) {
        when (response) {
            is ProfileResponse -> {
                var dataProfile: DataProfile? = DataProfile()



                response.data?.forEach {
                    dataProfile = it
                }

                Glide.with(this).load(dataProfile?.fotoProfile).into(imgFotoProfile)
                etNamaProfile.setText(dataProfile?.name)
                etEmailProfile.setText(dataProfile?.email)
                tvRoleProfile.text = dataProfile?.namaRole
                etDeskripsiProfile.setText(dataProfile?.desc)
                tvUsernameProfile.text = dataProfile?.username


                linearLayoutJenisProfile.removeAllViews()
                dataProfile?.jenis?.forEach {
                    val view = LayoutInflater.from(this).inflate(R.layout.item_simple_checkbox, linearLayoutJenisProfile, false)
                    view.checkbox.isChecked = true
                    view.checkbox.text = it?.jenis
                    view.checkbox.setOnCheckedChangeListener { _, checked ->
                        view.checkbox.isChecked = !checked
                    }
                    linearLayoutJenisProfile.addView(view)
                }
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

        progressDialog.dismiss()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            filePath = FilePath.getPath(this, fileUri as Uri)

            if (!filePath.isNullOrEmpty()) {
//                tvTextNameFile.text = filePath?.substring(filePath!!.lastIndexOf("/")+1)
                toastNotify("Foto berhasil dipilih\nSilahkan menyimpan perubahan")
            }

            toastNotify(filePath)
        }

        if (requestCode == PHOTO_SIGNATURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            var ttdPath: String? = null
            ttdPath = FilePath.getPath(this, fileUri as Uri)

            if (!ttdPath.isNullOrEmpty()) {
//                tvTextNameTtd.text = ttdPath?.substring(ttdPath!!.lastIndexOf("/")+1)
                toastNotify("Foto berhasil dipilih\nSilahkan menyimpan perubahan")
                toastNotify(ttdPath)
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

        val dialogRootView =
            View.inflate(this, R.layout.dialog_choose_signature_profile, null)

        dialogRootView.tvCreateSignatureProfile.setOnClickListener {
            showDialogSignaturePad()
            alertDialog.hide()
        }

        dialogRootView.tvGetSignatureProfile.setOnClickListener {
            val selectTtd = Intent(Intent.ACTION_PICK)
            selectTtd.setType("image/*")
            startActivityForResult(selectTtd, PHOTO_SIGNATURE_REQUEST_CODE)

            alertDialog.hide()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

    private fun showDialogSignaturePad() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Unggah Tanda Tangan")

        val alertDialog = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_signature_pad_profile, null)

        dialogRootView.signaturePadProfile.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
            }

            override fun onClear() {
                dialogRootView.btnSaveTtdProfile.isEnabled = false
                dialogRootView.btnClearTtdProfile.isEnabled = false
            }

            override fun onSigned() {
                dialogRootView.btnSaveTtdProfile.isEnabled = true
                dialogRootView.btnClearTtdProfile.isEnabled = true
            }

        })

        dialogRootView.btnSaveTtdProfile.setOnClickListener {
            val ttdBitmap = dialogRootView.signaturePadProfile.transparentSignatureBitmap
            fileTtd = Signature().saveSignature(this, ttdBitmap)
            if (fileTtd != null) {
                toastNotify("Tanda tangan telah dibuat\nSilahkan menyimpan perubahan")
            }
            alertDialog.hide()
        }

        dialogRootView.btnClearTtdProfile.setOnClickListener {
            dialogRootView.signaturePadProfile.clear()
        }

        alertDialog.setView(dialogRootView)
        alertDialog.show()
    }

}
