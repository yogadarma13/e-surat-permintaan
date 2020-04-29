package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.BatalkanSP
import com.e_suratpermintaan.core.domain.entities.responses.BatalkanSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class CancelSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_user: String, id: String): Observable<BatalkanSPResponse> =
        suratPermintaanRepository.cancel(id_user, id)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}