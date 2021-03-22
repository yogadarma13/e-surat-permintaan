package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.RollbackItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class RollbackItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        idUser: String,
        idSp: String,
        idItem: String
    ): Observable<RollbackItemSPResponse> =
        itemSuratPermintaanRepository.rollbackItem(idUser, idSp, idItem)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}