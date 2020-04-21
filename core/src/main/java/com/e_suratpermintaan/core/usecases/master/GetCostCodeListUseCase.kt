package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetCostCodeListUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_user: String): Observable<MasterCCResponse> =
        masterProyekRepository.getCostCodeList(id_user)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}