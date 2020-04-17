package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import io.reactivex.rxjava3.core.Observable

interface ProfileDataSource {

    fun getProfile(id_user: String): Observable<ProfileResponse>

}