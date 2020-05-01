package com.e_suratpermintaan.core.usecases.master.filter

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisPermintaanFilterOptionResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetJenisPermintaanFilterOptionUseCase(
    private val masterProyekRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_user: String): Observable<MasterJenisPermintaanFilterOptionResponse> =
        masterProyekRepository.getJenisPermintaanFilterOptionList(id_user)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}