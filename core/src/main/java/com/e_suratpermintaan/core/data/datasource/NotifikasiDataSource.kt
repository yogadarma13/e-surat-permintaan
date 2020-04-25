package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.ReadNotifikasi
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.e_suratpermintaan.core.domain.entities.responses.ReadNotifikasiResponse
import io.reactivex.rxjava3.core.Observable

interface NotifikasiDataSource {

    fun getNotifikasiList(id_user: String): Observable<NotifikasiResponse>

    fun readNotifikasi(readNotifikasi: ReadNotifikasi): Observable<ReadNotifikasiResponse>
}