package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DataAllResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import io.reactivex.rxjava3.core.Observable

class SuratPermintaanRepository(private val dataSource: SuratPermintaanDataSource) :
    SuratPermintaanDataSource {

    override fun add(id_proyek : String, jenis: String, id_user : String): Observable<CreateSPResponse> = dataSource.add(id_proyek, jenis, id_user)

    override fun readAllData(id_user: String): Observable<DataAllResponse> =
        dataSource.readAllData(id_user)

    override fun readMyData(id_user: String): Observable<MyDataResponse> =
        dataSource.readMyData(id_user)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> = dataSource.remove(id_sp)

}