package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileRepository(private val profileDataSource: ProfileDataSource) :
    ProfileDataSource {

    override fun getProfile(id_user: String): Observable<ProfileResponse> =
        profileDataSource.getProfile(id_user)

    override fun editProfile(
        id: RequestBody,
        email: RequestBody,
        passwordLast: RequestBody,
        passwordNew: RequestBody,
        name: RequestBody,
        desc: RequestBody,
        file: MultipartBody.Part?,
        ttd: MultipartBody.Part?
    ): Observable<EditProfileResponse> =
        profileDataSource.editProfile(id, email, passwordLast, passwordNew, name, desc, file, ttd)
}