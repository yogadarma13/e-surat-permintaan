package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.AjukanSP
import com.e_suratpermintaan.core.domain.entities.responses.AjukanSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class AjukanSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(ajukanSP: AjukanSP): Observable<AjukanSPResponse> =
        suratPermintaanRepository.ajukan(ajukanSP)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}