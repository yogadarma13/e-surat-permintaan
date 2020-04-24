package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.EditSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        id: RequestBody,
        file: MultipartBody.Part,
        id_user: RequestBody
    ): Observable<EditSPResponse> =
        suratPermintaanRepository.edit(id, file, id_user)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}