package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SuratPermintaanDataSource {
    fun add(createSP: CreateSP): Observable<CreateSPResponse>

    fun readAllData(
        idUser: String,
        idProyek: String,
        idJenis: String,
        idStatus: String
    ): Observable<DataAllResponse>

    fun readMyData(
        idUser: String,
        idProyek: String,
        idJenis: String
    ): Observable<MyDataResponse>

    fun remove(idSp: String): Observable<DeleteSPResponse>

    fun readDetail(idSp: String, idUser: String): Observable<DetailSPResponse>

    fun edit(
        id: RequestBody,
        file: MultipartBody.Part,
        idUser: RequestBody
    ): Observable<EditSPResponse>

    fun verifikasi(
        idUser: RequestBody,
        id: RequestBody,
        status: RequestBody,
        catatan: RequestBody,
        file: MultipartBody.Part
    ): Observable<VerifikasiSPResponse>

    fun ajukan(
        idUser: RequestBody,
        id: RequestBody,
        file: MultipartBody.Part
    ): Observable<AjukanSPResponse>

    fun cancel(idUser: String, id: String): Observable<BatalkanSPResponse>

    fun readHistory(idSp: String): Observable<HistorySPResponse>

    fun saveEdit(id: String, idUser: String): Observable<SimpanEditSPResponse>
}