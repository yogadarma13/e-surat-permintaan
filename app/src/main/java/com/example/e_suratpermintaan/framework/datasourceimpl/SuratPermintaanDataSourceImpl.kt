package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.*
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SuratPermintaanDataSourceImpl(private val networkApi: NetworkApi) :
    SuratPermintaanDataSource {

    override fun add(createSP: CreateSP): Observable<CreateSPResponse> =
        networkApi.createSP(createSP)

    override fun readAllData(id_user: String): Observable<DataAllResponse> =
        networkApi.getDataAll(id_user)

    override fun readMyData(id_user: String): Observable<MyDataResponse> =
        networkApi.getMyData(id_user)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> =
        networkApi.deleteSP(id_sp)

    override fun readDetail(id_sp: String, id_user: String): Observable<DetailSPResponse> =
        networkApi.getDetailSP(id_sp, id_user)

    override fun edit(
        id: RequestBody,
        file: MultipartBody.Part,
        id_user: RequestBody
    ): Observable<EditSPResponse> =
        networkApi.editSP(id, file, id_user)

    override fun verifikasi(id_user: String, id: String, status: String, catatan: String): Observable<VerifikasiSPResponse> =
        networkApi.verifikasiSP(id_user, id, status, catatan)

    override fun ajukan(id_user: String, id: String): Observable<AjukanSPResponse> =
        networkApi.ajukanSP(id_user, id)

    override fun cancel(id_user: String, id:String): Observable<BatalkanSPResponse> =
        networkApi.batalkanSP(id_user, id)

    override fun readHistory(id_sp: String): Observable<HistorySPResponse> =
        networkApi.getHistorySP(id_sp)

}