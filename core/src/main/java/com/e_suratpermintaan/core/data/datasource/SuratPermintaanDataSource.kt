package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.*
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SuratPermintaanDataSource {
    fun add(createSP: CreateSP): Observable<CreateSPResponse>

    fun readAllData(id_user: String): Observable<DataAllResponse>

    fun readMyData(id_user: String): Observable<MyDataResponse>

    fun remove(id_sp: String): Observable<DeleteSPResponse>

    fun readDetail(id_sp: String, id_user: String): Observable<DetailSPResponse>

    fun edit(
        id: RequestBody,
        file: MultipartBody.Part,
        id_user: RequestBody
    ): Observable<EditSPResponse>

    fun verifikasi(id_user: String, id: String, status: String, catatan: String): Observable<VerifikasiSPResponse>

    fun ajukan(id_user: String, id: String): Observable<AjukanSPResponse>

    fun cancel(id_user: String, id: String): Observable<BatalkanSPResponse>

    fun readHistory(id_sp: String): Observable<HistorySPResponse>
}