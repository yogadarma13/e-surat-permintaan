package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.responses.PenugasanItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class SetPenugasanItemUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(penugasanItemSP: PenugasanItemSP): Observable<PenugasanItemSPResponse> =
        itemSuratPermintaanRepository.setPenugasanItem(penugasanItemSP)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}