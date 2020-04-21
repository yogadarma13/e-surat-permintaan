package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DataAllResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class SuratPermintaanDataSourceImpl(private val networkApi: NetworkApi) :
    SuratPermintaanDataSource {

    override fun add(sp: CreateSP): Observable<CreateSPResponse> = networkApi.createSP(sp)

    override fun readAllData(id_user: String): Observable<DataAllResponse> =
        networkApi.getDataAll(id_user)

    override fun readMyData(id_user: String): Observable<MyDataResponse> =
        networkApi.getMyData(id_user)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> = networkApi.deleteSP(id_sp)

}