package com.e_suratpermintaan.core.usecases.notifikasi

import com.e_suratpermintaan.core.data.repository.NotifikasiRepository
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class GetNotifikasiListUseCase(
    private val notifikasiRepository: NotifikasiRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_user: String): Observable<NotifikasiResponse> =
        notifikasiRepository.getNotifikasiList(id_user)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}