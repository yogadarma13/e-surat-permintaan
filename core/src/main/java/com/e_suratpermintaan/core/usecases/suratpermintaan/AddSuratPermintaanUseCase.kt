package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class AddSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_proyek: String, jenis: String, id_user: String): Observable<CreateSPResponse> =
        suratPermintaanRepository.add(id_proyek, jenis, id_user)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}