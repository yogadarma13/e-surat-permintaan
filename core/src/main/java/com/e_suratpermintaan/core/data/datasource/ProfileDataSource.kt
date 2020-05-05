package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileDataSource {

    fun getProfile(id_user: String): Observable<ProfileResponse>

    fun editProfile(id: RequestBody,
                    email: RequestBody,
                    passwordLast: RequestBody,
                    passwordNew: RequestBody,
                    name: RequestBody,
                    desc: RequestBody,
                    file: MultipartBody.Part?,
                    ttd: MultipartBody.Part?): Observable<EditProfileResponse>

}