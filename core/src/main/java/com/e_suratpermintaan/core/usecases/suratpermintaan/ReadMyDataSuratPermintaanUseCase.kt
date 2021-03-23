package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadMyDataSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        idUser: String,
        idProyek: String,
        idJenis: String
    ): Observable<MyDataResponse> =
        suratPermintaanRepository.readMyData(idUser, idProyek, idJenis)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}