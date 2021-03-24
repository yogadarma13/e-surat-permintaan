package com.e_suratpermintaan.core.usecases.master.optionlist.filter

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisDataFilterOptionResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetJenisDataFilterOptionUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
//    fun invoke(): Observable<MasterJenisDataFilterOptionResponse> =
//        masterProyekRepository.getJenisDataFilterOptionList()
//            .subscribeOn(schedulerProvider.io)
//            .observeOn(schedulerProvider.mainThread)
}