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

    override fun readAllData(id_user: String): Observable<DataAllResponse> =
        dataSource.readAllData(id_user)

    override fun readMyData(
        id_user: String,
        proyek: String,
        status_permintaan: String,
        jenis_permintaan: String,
        jenis_data: String
    ): Observable<MyDataResponse> =
        dataSource.readMyData(id_user, proyek, status_permintaan, jenis_permintaan, jenis_data)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> =
        dataSource.remove(id_sp)

    override fun readDetail(id_sp: String, id_user: String): Observable<DetailSPResponse> =
        dataSource.readDetail(id_sp, id_user)

    override fun edit(id: RequestBody, file: MultipartBody.Part, id_user: RequestBody) =
        dataSource.edit(id, file, id_user)

    override fun verifikasi(
        id_user: RequestBody,
        id: RequestBody,
        status: RequestBody,
        catatan: RequestBody,
        file: MultipartBody.Part
    ): Observable<VerifikasiSPResponse> =
        dataSource.verifikasi(id_user, id, status, catatan, file)

    override fun ajukan(
        id_user: RequestBody,
        id: RequestBody,
        file: MultipartBody.Part
    ): Observable<AjukanSPResponse> =
        dataSource.ajukan(id_user, id, file)

    override fun cancel(id_user: String, id: String): Observable<BatalkanSPResponse> =
        dataSource.cancel(id_user, id)

    override fun readHistory(id_sp: String): Observable<HistorySPResponse> =
        dataSource.readHistory(id_sp)

    override fun saveEdit(id: String, id_user: String): Observable<SimpanEditSPResponse> =
        dataSource.saveEdit(id, id_user)

}