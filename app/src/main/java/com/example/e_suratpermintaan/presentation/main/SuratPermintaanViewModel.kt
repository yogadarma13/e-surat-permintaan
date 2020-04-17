package com.example.e_suratpermintaan.presentation.main

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.domain.usecases.suratpermintaan.AddSuratPermintaan
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadAllDataSuratPermintaan
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadMyDataSuratPermintaan
import com.e_suratpermintaan.domain.usecases.suratpermintaan.RemoveSuratPermintaan
import io.reactivex.rxjava3.core.Observable

class SuratPermintaanViewModel(
    private val addSuratPermintaan: AddSuratPermintaan,
    private val readAllDataSuratPermintaan: ReadAllDataSuratPermintaan,
    private val readMyDataSuratPermintaan: ReadMyDataSuratPermintaan,
    private val removeSuratPermintaan: RemoveSuratPermintaan
) : ViewModel(), SuratPermintaanDataSource {

    override fun add(sp: CreateSP): Observable<CreateSPResponse> = addSuratPermintaan.invoke(sp)

    override fun readAllData(id_user: String): Observable<MyDataResponse> =
        readAllDataSuratPermintaan.invoke(id_user)

    override fun readMyData(id_user: String): Observable<MyDataResponse> =
        readMyDataSuratPermintaan.invoke(id_user)

    override fun remove(id_sp: String): Observable<DeleteSPResponse> =
        removeSuratPermintaan.invoke(id_sp)

}