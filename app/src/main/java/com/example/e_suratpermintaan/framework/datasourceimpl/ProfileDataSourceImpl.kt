package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileDataSourceImpl(private val networkApi: NetworkApi) :

    ProfileDataSource {
    override fun getProfile(id_user: String): Observable<ProfileResponse> =
        networkApi.profileUser(id_user)

    override fun editProfile(
        id: RequestBody,
        email: RequestBody,
        passwordLast: RequestBody,
        passwordNew: RequestBody,
        name: RequestBody,
        desc: RequestBody,
        file: MultipartBody.Part?,
        ttd: MultipartBody.Part?
    ): Observable<EditProfileResponse> = networkApi.edit_profile(id, email, passwordLast, passwordNew, name, desc, file, ttd)

}