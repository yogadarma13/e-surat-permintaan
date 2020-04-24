package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.NotifikasiDataSource
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.e_suratpermintaan.core.domain.entities.responses.ReadNotifikasiResponse
import io.reactivex.rxjava3.core.Observable

class NotifikasiRepository(private val notifikasiDataSource: NotifikasiDataSource) :
    NotifikasiDataSource {

    override fun getNotifikasiList(id_user: String): Observable<NotifikasiResponse> =
        notifikasiDataSource.getNotifikasiList(id_user)

    override fun readNotifikasi(id_user: String, id: String): Observable<ReadNotifikasiResponse> =
        notifikasiDataSource.readNotifikasi(id_user, id)
}