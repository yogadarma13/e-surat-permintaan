package com.e_suratpermintaan.core.usecases.notifikasi

import com.e_suratpermintaan.core.data.repository.NotifikasiRepository
import com.e_suratpermintaan.core.domain.entities.requests.ReadNotifikasi
import com.e_suratpermintaan.core.domain.entities.responses.ReadNotifikasiResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadNotifikasiUseCase(
    private val notifikasiRepository: NotifikasiRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(readNotifikasi: ReadNotifikasi): Observable<ReadNotifikasiResponse> =
        notifikasiRepository.readNotifikasi(readNotifikasi)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}