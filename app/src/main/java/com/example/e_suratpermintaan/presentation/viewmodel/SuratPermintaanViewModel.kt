package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
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
    private val readHistorySuratPermintaanUseCase: ReadHistorySuratPermintaanUseCase,
    private val saveEditSuratPermintaanUseCase: SaveEditSuratPermintaanUseCase
) : ViewModel(), SuratPermintaanDataSource {

    override fun add(createSP: CreateSP): Observable<CreateSPResponse> =
        addSuratPermintaanUseCase.invoke(createSP)

    override fun readAllData(idUser: String): Observable<DataAllResponse> =
        readAllDataSuratPermintaanUseCase.invoke(idUser)

    override fun readMyData(
        idUser: String,
        idProyek: String,
        idJenis: String
    ): Observable<MyDataResponse> =
        readMyDataSuratPermintaanUseCase.invoke(idUser, idProyek, idJenis)

    override fun remove(idSp: String): Observable<DeleteSPResponse> =
        removeSuratPermintaanUseCase.invoke(idSp)

    override fun readDetail(idSp: String, idUser: String): Observable<DetailSPResponse> =
        readDetailSuratPermintaanUseCase.invoke(idSp, idUser)

    override fun edit(
        id: RequestBody,
        file: MultipartBody.Part,
        idUser: RequestBody
    ): Observable<EditSPResponse> =
        editSuratPermintaanUseCase.invoke(id, file, idUser)

    override fun verifikasi(
        idUser: RequestBody,
        id: RequestBody,
        status: RequestBody,
        catatan: RequestBody,
        file: MultipartBody.Part
    ): Observable<VerifikasiSPResponse> =
        verifikasiSuratPermintaanUseCase.invoke(idUser, id, status, catatan, file)

    override fun ajukan(
        idUser: RequestBody,
        id: RequestBody,
        file: MultipartBody.Part
    ): Observable<AjukanSPResponse> =
        ajukanSuratPermintaanUseCase.invoke(idUser, id, file)

    override fun cancel(idUser: String, id: String): Observable<BatalkanSPResponse> =
        cancelSuratPermintaanUseCase.invoke(idUser, id)

    override fun readHistory(idSp: String): Observable<HistorySPResponse> =
        readHistorySuratPermintaanUseCase.invoke(idSp)

    override fun saveEdit(id: String, idUser: String): Observable<SimpanEditSPResponse> =
        saveEditSuratPermintaanUseCase.invoke(id, idUser)

}