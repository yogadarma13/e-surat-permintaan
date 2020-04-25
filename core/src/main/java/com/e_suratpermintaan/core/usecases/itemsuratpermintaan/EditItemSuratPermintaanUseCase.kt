package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.EditItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class EditItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse> =
        itemSuratPermintaanRepository.editItem(updateItemSP)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}