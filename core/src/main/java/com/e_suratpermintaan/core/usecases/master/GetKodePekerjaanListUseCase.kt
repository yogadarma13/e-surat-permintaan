package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterKodePekerjaanResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetKodePekerjaanListUseCase(
    private val masterRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: String): Observable<MasterKodePekerjaanResponse> =
        masterRepository.getKodePekerjaanList(id)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}