package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.VerifikasiSP
import com.e_suratpermintaan.core.domain.entities.responses.VerifikasiSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class VerifikasiSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(verifikasiSP: VerifikasiSP): Observable<VerifikasiSPResponse> =
        suratPermintaanRepository.verifikasi(verifikasiSP)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}