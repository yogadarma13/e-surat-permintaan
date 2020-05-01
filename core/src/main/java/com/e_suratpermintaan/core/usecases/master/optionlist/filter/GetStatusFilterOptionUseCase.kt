package com.e_suratpermintaan.core.usecases.master.optionlist.filter

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterStatusFilterOptionResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetStatusFilterOptionUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(): Observable<MasterStatusFilterOptionResponse> =
        masterProyekRepository.getStatusFilterOptionList()
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}