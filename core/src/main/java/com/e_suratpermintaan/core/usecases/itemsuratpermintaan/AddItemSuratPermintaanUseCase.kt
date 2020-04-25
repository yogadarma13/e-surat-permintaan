package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class AddItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun invoke(createItemSP: CreateItemSP): Observable<CreateItemSPResponse> =
        itemSuratPermintaanRepository.addItem(createItemSP)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}