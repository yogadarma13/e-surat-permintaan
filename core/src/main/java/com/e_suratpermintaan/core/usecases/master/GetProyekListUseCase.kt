package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterProyekResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetProyekListUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
//    fun invoke(id_user: String): Observable<MasterProyekResponse> =
//        masterProyekRepository.getProyekList(id_user)
//            .subscribeOn(schedulerProvider.io)
//            .observeOn(schedulerProvider.mainThread)
}