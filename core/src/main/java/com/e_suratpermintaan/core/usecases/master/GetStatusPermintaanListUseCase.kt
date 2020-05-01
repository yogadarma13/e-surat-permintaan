package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterStatusPermintaanResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetStatusPermintaanListUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(): Observable<MasterStatusPermintaanResponse> =
        masterProyekRepository.getStatusPermintaanList()
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}