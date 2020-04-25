package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.*
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SuratPermintaanRepository(private val dataSource: SuratPermintaanDataSource) :
    SuratPermintaanDataSource {

    override fun add(createSP: CreateSP
    ): Observable<CreateSPResponse> = dataSource.add(createSP)

    override fun readAllData(id_user: String): Observable<DataAllResponse> =
        dataSource.readAllData(id_user)

    override fun readMyData(id_user: String): Observable<MyDataResponse> =
        dataSource.readMyData(id_user)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> =
        dataSource.remove(id_sp)

    override fun readDetail(detailSP: DetailSP): Observable<DetailSPResponse> =
        dataSource.readDetail(detailSP)

    override fun edit(id: RequestBody, file: MultipartBody.Part, id_user: RequestBody) =
        dataSource.edit(id, file, id_user)

    override fun verifikasi(verifikasiSP: VerifikasiSP
    ): Observable<VerifikasiSPResponse> =
        dataSource.verifikasi(
            verifikasiSP
        )

    override fun ajukan(ajukanSP: AjukanSP): Observable<AjukanSPResponse> =
        dataSource.ajukan(ajukanSP)

    override fun cancel(batalkanSP: BatalkanSP): Observable<BatalkanSPResponse> =
        dataSource.cancel(batalkanSP)

    override fun readHistory(id_sp: String): Observable<HistorySPResponse> =
        dataSource.readHistory(id_sp)

}