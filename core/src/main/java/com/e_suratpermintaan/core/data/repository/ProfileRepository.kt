package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import io.reactivex.rxjava3.core.Observable

class ProfileRepository(private val profileDataSource: ProfileDataSource) :
    ProfileDataSource {

    override fun getProfile(id_user: String): Observable<ProfileResponse> =
        profileDataSource.getProfile(id_user)

}