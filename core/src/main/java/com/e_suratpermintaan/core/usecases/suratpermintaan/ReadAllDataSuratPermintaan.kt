package com.e_suratpermintaan.domain.usecases.suratpermintaan

import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import io.reactivex.rxjava3.core.Observable

class ReadAllDataSuratPermintaan(private val suratPermintaanRepository: SuratPermintaanRepository) {
    fun invoke(id_user: String): Observable<MyDataResponse> =
        suratPermintaanRepository.readAllData(id_user)
}