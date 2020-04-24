package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.NotifikasiDataSource
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.e_suratpermintaan.core.domain.entities.responses.ReadNotifikasiResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class NotifikasiDataSourceImpl(private val networkApi: NetworkApi) : NotifikasiDataSource {
    override fun getNotifikasiList(id_user: String): Observable<NotifikasiResponse> =
        networkApi.getNotifikasi(id_user)

    override fun readNotifikasi(id_user: String, id: String): Observable<ReadNotifikasiResponse> =
        networkApi.readNotifikasi(id_user, id)
}