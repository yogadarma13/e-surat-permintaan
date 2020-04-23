package com.e_suratpermintaan.core.usecases.filelampiran

import com.e_suratpermintaan.core.data.repository.FileLampiranRepository
import com.e_suratpermintaan.core.domain.entities.responses.DeleteFileLampiranResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class RemoveFileLampiranUseCase(
    private val fileLampiranRepository: FileLampiranRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id_file: String): Observable<DeleteFileLampiranResponse> =
        fileLampiranRepository.removeFile(id_file)
            .observeOn(schedulerProvider.mainThread)
            .subscribeOn(schedulerProvider.io)
}