package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.DetailSP
import com.e_suratpermintaan.core.domain.entities.responses.DetailSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadDetailSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_sp: String, id_user: String): Observable<DetailSPResponse> =
        suratPermintaanRepository.readDetail(id_sp, id_user)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}