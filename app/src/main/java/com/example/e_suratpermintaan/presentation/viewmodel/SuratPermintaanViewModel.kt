package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.*
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.usecases.suratpermintaan.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SuratPermintaanViewModel(
    private val addSuratPermintaanUseCase: AddSuratPermintaanUseCase,
    private val readAllDataSuratPermintaanUseCase: ReadAllDataSuratPermintaanUseCase,
    private val readMyDataSuratPermintaanUseCase: ReadMyDataSuratPermintaanUseCase,
    private val removeSuratPermintaanUseCase: RemoveSuratPermintaanUseCase,
    private val readDetailSuratPermintaanUseCase: ReadDetailSuratPermintaanUseCase,
    private val editSuratPermintaanUseCase: EditSuratPermintaanUseCase,
    private val verifikasiSuratPermintaanUseCase: VerifikasiSuratPermintaanUseCase,
    private val ajukanSuratPermintaanUseCase: AjukanSuratPermintaanUseCase,
    private val cancelSuratPermintaanUseCase: CancelSuratPermintaanUseCase,
    private val readHistorySuratPermintaanUseCase: ReadHistorySuratPermintaanUseCase
) : ViewModel(), SuratPermintaanDataSource {

    override fun add(createSP: CreateSP): Observable<CreateSPResponse> =
        addSuratPermintaanUseCase.invoke(createSP)

    override fun readAllData(id_user: String): Observable<DataAllResponse> =
        readAllDataSuratPermintaanUseCase.invoke(id_user)

    override fun readMyData(id_user: String): Observable<MyDataResponse> =
        readMyDataSuratPermintaanUseCase.invoke(id_user)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> =
        removeSuratPermintaanUseCase.invoke(id_sp)

    override fun readDetail(detailSP: DetailSP): Observable<DetailSPResponse> =
        readDetailSuratPermintaanUseCase.invoke(detailSP)

    override fun edit(
        id: RequestBody,
        file: MultipartBody.Part,
        id_user: RequestBody
    ): Observable<EditSPResponse> =
        editSuratPermintaanUseCase.invoke(id, file, id_user)

    override fun verifikasi(verifikasiSP: VerifikasiSP): Observable<VerifikasiSPResponse> =
        verifikasiSuratPermintaanUseCase.invoke(verifikasiSP)

    override fun ajukan(ajukanSP: AjukanSP): Observable<AjukanSPResponse> =
        ajukanSuratPermintaanUseCase.invoke(ajukanSP)

    override fun cancel(batalkanSP: BatalkanSP): Observable<BatalkanSPResponse> =
        cancelSuratPermintaanUseCase.invoke(batalkanSP)

    override fun readHistory(id_sp: String): Observable<HistorySPResponse> =
        readHistorySuratPermintaanUseCase.invoke(id_sp)

}