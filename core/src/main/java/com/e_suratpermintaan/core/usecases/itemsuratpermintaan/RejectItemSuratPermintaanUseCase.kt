package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.RejectItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class RejectItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        idUser: String,
        idSp: String,
        idItem: String,
        note: String
    ): Observable<RejectItemSPResponse> =
        itemSuratPermintaanRepository.rejectItem(idUser, idSp, idItem, note)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}