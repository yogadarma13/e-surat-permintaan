package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterPersyaratanResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetPersyaratanListUseCase(
    private val masterPersyaratanRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: String): Observable<MasterPersyaratanResponse> =
        masterPersyaratanRepository.getPersyaratanList(id)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}