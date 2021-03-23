package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SuratPermintaanDataSourceImpl(private val networkApi: NetworkApi) :
    SuratPermintaanDataSource {

    override fun add(createSP: CreateSP): Observable<CreateSPResponse> =
        networkApi.createSP(createSP)

    override fun readAllData(idUser: String): Observable<DataAllResponse> =
        networkApi.getDataAll(idUser)

    override fun readMyData(
        idUser: String,
        idProyek: String,
        idJenis: String
    ): Observable<MyDataResponse> =
        networkApi.getMyData(idUser, idProyek, idJenis)

    override fun remove(idSp: String): Observable<DeleteSPResponse> =
        networkApi.deleteSP(idSp)

    override fun readDetail(idSp: String, idUser: String): Observable<DetailSPResponse> =
        networkApi.getDetailSP(idSp, idUser)

    override fun edit(
        id: RequestBody,
        file: MultipartBody.Part,
        idUser: RequestBody
    ): Observable<EditSPResponse> =
        networkApi.editSP(id, file, idUser)

    override fun verifikasi(
        idUser: RequestBody,
        id: RequestBody,
        status: RequestBody,
        catatan: RequestBody,
        file: MultipartBody.Part
    ): Observable<VerifikasiSPResponse> =
        networkApi.verifikasiSP(idUser, id, status, catatan, file)

    override fun ajukan(
        idUser: RequestBody,
        id: RequestBody,
        file: MultipartBody.Part
    ): Observable<AjukanSPResponse> =
        networkApi.ajukanSP(idUser, id, file)

    override fun cancel(idUser: String, id: String): Observable<BatalkanSPResponse> =
        networkApi.batalkanSP(idUser, id)

    override fun readHistory(idSp: String): Observable<HistorySPResponse> =
        networkApi.getHistorySP(idSp)

    override fun saveEdit(id: String, idUser: String): Observable<SimpanEditSPResponse> =
        networkApi.simpanEditSP(id, idUser)

}