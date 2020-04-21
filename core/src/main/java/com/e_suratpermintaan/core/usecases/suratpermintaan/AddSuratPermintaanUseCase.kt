package com.e_suratpermintaan.domain.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class AddSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(sp: CreateSP): Observable<CreateSPResponse> =
        suratPermintaanRepository.add(sp)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}