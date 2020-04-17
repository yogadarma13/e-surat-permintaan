package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class ProfileDataSourceImpl(private val networkApi: NetworkApi) :

    ProfileDataSource {
    override fun getProfile(id_user: String): Observable<ProfileResponse> =
        networkApi.profileUser(id_user)

}