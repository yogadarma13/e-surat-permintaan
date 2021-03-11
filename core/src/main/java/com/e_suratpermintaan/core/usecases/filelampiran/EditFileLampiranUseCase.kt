package com.e_suratpermintaan.core.usecases.filelampiran

import com.e_suratpermintaan.core.data.repository.FileLampiranRepository
import com.e_suratpermintaan.core.domain.entities.responses.EditFileLampiranResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditFileLampiranUseCase(
    private val fileLampiranRepository: FileLampiranRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        keterangan: RequestBody,
        file: MultipartBody.Part,
        id_file: RequestBody
    ): Observable<EditFileLampiranResponse> =
        fileLampiranRepository.editFile(keterangan, file, id_file)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}