package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.DetailItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadDetailItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: String): Observable<DetailItemSPResponse> =
        itemSuratPermintaanRepository.readDetailItem(id)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}