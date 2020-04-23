package com.e_suratpermintaan.core.usecases.filelampiran

import com.e_suratpermintaan.core.data.repository.FileLampiranRepository
import com.e_suratpermintaan.core.domain.entities.responses.CreateFileLampiranResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddFileLampiranUseCase(
    private val fileLampiranRepository: FileLampiranRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: RequestBody, keterangan: RequestBody, file: MultipartBody.Part): Observable<CreateFileLampiranResponse> =
        fileLampiranRepository.addFile(id, keterangan, file)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}