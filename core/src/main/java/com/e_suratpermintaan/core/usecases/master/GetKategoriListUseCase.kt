package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.KategoriResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetKategoriListUseCase(
    private val masterRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(): Observable<KategoriResponse> = masterRepository.getKategoriList()
        .subscribeOn(schedulerProvider.io)
        .observeOn(schedulerProvider.mainThread)
}