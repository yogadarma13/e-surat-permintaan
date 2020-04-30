package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisDataPermintaanResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetJenisDataPermintaanListUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(): Observable<MasterJenisDataPermintaanResponse> =
        masterProyekRepository.getJenisDataPermintaanList()
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}