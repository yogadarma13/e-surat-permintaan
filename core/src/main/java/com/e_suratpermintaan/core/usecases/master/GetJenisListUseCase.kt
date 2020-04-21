package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisProyekResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetJenisListUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
){
    fun invoke(id_user: String): Observable<MasterJenisProyekResponse> =
        masterProyekRepository.getJenisList(id_user)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}