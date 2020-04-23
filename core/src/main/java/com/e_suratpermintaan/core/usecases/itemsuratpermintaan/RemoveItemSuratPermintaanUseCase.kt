package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.DeleteItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class RemoveItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: String): Observable<DeleteItemSPResponse> =
        itemSuratPermintaanRepository.removeItem(id)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}