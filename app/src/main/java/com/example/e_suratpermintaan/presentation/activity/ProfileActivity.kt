package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.FilePath
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_surat_permintaan.*
import kotlinx.android.synthetic.main.activity_profile.*
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
    private var id: String? = null
    private lateinit var partFile: MultipartBody.Part
    private lateinit var partTtd: MultipartBody.Part

    override fun layoutId(): Int = R.layout.activity_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = profilePreference.getProfile()?.id

        init()

    }

    private fun init() {
        setupListeners()
        initApiRequest()
    }

    private fun initApiRequest() {
        disposable = provileViewModel.getProfile(id.toString())
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun setupListeners() {
        btnSimpanProfile.setOnClickListener {
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
            val selectTtd = Intent(Intent.ACTION_PICK)
            selectTtd.setType("image/*")
            startActivityForResult(selectTtd, PHOTO_SIGNATURE_REQUEST_CODE)
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

                var jenis = ""

                dataProfile?.jenis?.forEach {
                    jenis += "${it?.jenis}\n"
                }

                tvJenisProfile.text = jenis

            }

            is EditProfileResponse -> {
                toastNotify(response.message)
                initApiRequest()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            val filePath = FilePath.getPath(this, fileUri as Uri)

            toastNotify(filePath)

            val file = File(filePath)

            val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

            partFile = MultipartBody.Part.createFormData("file", file.name, fileReqBody)
        }

        if (requestCode == PHOTO_SIGNATURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            val filePath = FilePath.getPath(this, fileUri as Uri)

            toastNotify(filePath)

            val file = File(filePath)

            val fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

            partTtd = MultipartBody.Part.createFormData("ttd", file.name, fileReqBody)
        }
    }

    private fun getRealPathImageFromURI(uri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, uri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground() as Cursor
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

}
