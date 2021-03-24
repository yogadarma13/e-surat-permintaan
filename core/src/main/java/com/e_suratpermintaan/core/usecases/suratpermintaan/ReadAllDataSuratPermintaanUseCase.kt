package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.DataAllResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadAllDataSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        idUser: String,
        idProyek: String,
        idJenis: String,
        idStatus: String
    ): Observable<DataAllResponse> =
        suratPermintaanRepository.readAllData(idUser, idProyek, idJenis, idStatus)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}