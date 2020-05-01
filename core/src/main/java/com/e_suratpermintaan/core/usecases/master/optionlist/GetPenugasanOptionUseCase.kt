package com.e_suratpermintaan.core.usecases.master.optionlist

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterPenugasanOptionResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetPenugasanOptionUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(): Observable<MasterPenugasanOptionResponse> =
        masterProyekRepository.getPenugasanOptionList()
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}