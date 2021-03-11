package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.AjukanSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AjukanSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        id_user: RequestBody,
        id: RequestBody,
        file: MultipartBody.Part
    ): Observable<AjukanSPResponse> =
        suratPermintaanRepository.ajukan(id_user, id, file)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}