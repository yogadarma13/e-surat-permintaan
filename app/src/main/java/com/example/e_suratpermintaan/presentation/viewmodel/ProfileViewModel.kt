package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.e_suratpermintaan.core.usecases.profile.EditProfileUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel(), ProfileDataSource {

    override fun getProfile(id_user: String): Observable<ProfileResponse> =
        getProfileUseCase.invoke(id_user)

    override fun editProfile(
        id: RequestBody,
        email: RequestBody,
        passwordLast: RequestBody,
        passwordNew: RequestBody,
        name: RequestBody,
        desc: RequestBody,
        file: MultipartBody.Part,
        ttd: MultipartBody.Part
    ): Observable<EditProfileResponse> =
        editProfileUseCase.invoke(id, email, passwordLast, passwordNew, name, desc, file, ttd)

}