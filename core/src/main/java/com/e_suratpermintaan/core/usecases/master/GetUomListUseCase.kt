package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterUOMResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetUomListUseCase(
    private val masterRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: String): Observable<MasterUOMResponse> =
        masterRepository.getUomList(id)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}