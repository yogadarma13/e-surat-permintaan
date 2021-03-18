package com.e_suratpermintaan.core.usecases.master

import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.domain.entities.responses.MasterItemCodeResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class GetItemCodeListUseCase(
    private val masterRepository: MasterRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: String, keyword: String): Observable<MasterItemCodeResponse> =
        masterRepository.getItemCodeLlist(id, keyword)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}