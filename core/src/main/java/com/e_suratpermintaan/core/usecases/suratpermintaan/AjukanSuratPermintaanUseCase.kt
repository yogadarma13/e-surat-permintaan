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
    fun invoke(id_user: String, id: String): Observable<AjukanSPResponse> =
        suratPermintaanRepository.ajukan(id_user, id)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}