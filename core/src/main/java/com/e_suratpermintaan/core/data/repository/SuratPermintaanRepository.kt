package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SuratPermintaanRepository(private val dataSource: SuratPermintaanDataSource) :
    SuratPermintaanDataSource {

    override fun add(createSP: CreateSP): Observable<CreateSPResponse> = dataSource.add(createSP)

    override fun readAllData(
        idUser: String,
        idProyek: String,
        idJenis: String,
        idStatus: String
    ): Observable<DataAllResponse> =
        dataSource.readAllData(idUser, idProyek, idJenis, idStatus)

    override fun readMyData(
        idUser: String,
        idProyek: String,
        idJenis: String
    ): Observable<MyDataResponse> =
        dataSource.readMyData(idUser, idProyek, idJenis)

    override fun remove(idSp: String): Observable<DeleteSPResponse> =
        dataSource.remove(idSp)

    override fun readDetail(idSp: String, idUser: String): Observable<DetailSPResponse> =
        dataSource.readDetail(idSp, idUser)

    override fun edit(id: RequestBody, file: MultipartBody.Part, idUser: RequestBody) =
        dataSource.edit(id, file, idUser)

    override fun verifikasi(
        idUser: RequestBody,
        id: RequestBody,
        status: RequestBody,
        catatan: RequestBody,
        file: MultipartBody.Part
    ): Observable<VerifikasiSPResponse> =
        dataSource.verifikasi(idUser, id, status, catatan, file)

    override fun ajukan(
        idUser: RequestBody,
        id: RequestBody,
        file: MultipartBody.Part
    ): Observable<AjukanSPResponse> =
        dataSource.ajukan(idUser, id, file)

    override fun cancel(idUser: String, id: String): Observable<BatalkanSPResponse> =
        dataSource.cancel(idUser, id)

    override fun readHistory(idSp: String): Observable<HistorySPResponse> =
        dataSource.readHistory(idSp)

    override fun saveEdit(id: String, idUser: String): Observable<SimpanEditSPResponse> =
        dataSource.saveEdit(id, idUser)

}