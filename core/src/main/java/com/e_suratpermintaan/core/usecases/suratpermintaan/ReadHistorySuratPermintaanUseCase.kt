package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.HistorySPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadHistorySuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_sp: String): Observable<HistorySPResponse> =
        suratPermintaanRepository.readHistory(id_sp)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}