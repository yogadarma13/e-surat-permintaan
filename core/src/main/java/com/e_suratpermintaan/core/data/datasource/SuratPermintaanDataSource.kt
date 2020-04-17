package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import io.reactivex.rxjava3.core.Observable

interface SuratPermintaanDataSource {
    fun add(sp: CreateSP): Observable<CreateSPResponse>

    fun readAllData(id_user: String): Observable<MyDataResponse>

    fun readMyData(id_user: String): Observable<MyDataResponse>

    fun remove(id_sp: String): Observable<DeleteSPResponse>
}