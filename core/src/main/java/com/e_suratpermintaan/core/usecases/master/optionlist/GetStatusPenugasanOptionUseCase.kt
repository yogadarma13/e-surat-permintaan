package com.e_suratpermintaan.core.usecases.master.optionlist

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterStatusPenugasanOptionResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetStatusPenugasanOptionUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(): Observable<MasterStatusPenugasanOptionResponse> =
        masterProyekRepository.getStatusPenugasanOptionList()
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}