package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.VerifikasiSP
import com.e_suratpermintaan.core.domain.entities.responses.VerifikasiSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class VerifikasiSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_user: RequestBody, id: RequestBody, status: RequestBody, catatan: RequestBody, file: MultipartBody.Part): Observable<VerifikasiSPResponse> =
        suratPermintaanRepository.verifikasi(id_user, id, status, catatan, file)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}