package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.ProcessItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ProcessItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(idSp: String, idItem: String, idUser: String): Observable<ProcessItemSPResponse> =
        itemSuratPermintaanRepository.processItem(idSp, idItem, idUser)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}