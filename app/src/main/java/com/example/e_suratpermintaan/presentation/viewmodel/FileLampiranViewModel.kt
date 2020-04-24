package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.FileLampiranDataSource
import com.e_suratpermintaan.core.domain.entities.responses.CreateFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditFileLampiranResponse
import com.e_suratpermintaan.core.usecases.filelampiran.AddFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.EditFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.RemoveFileLampiranUseCase
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FileLampiranViewModel(
    private val addFileLampiranUseCase: AddFileLampiranUseCase,
    private val editFileLampiranUseCase: EditFileLampiranUseCase,
    private val removeFileLampiranUseCase: RemoveFileLampiranUseCase
) : ViewModel(), FileLampiranDataSource {
    override fun addFile(
        id: RequestBody,
        keterangan: RequestBody,
        file: MultipartBody.Part
    ): Observable<CreateFileLampiranResponse> =
        addFileLampiranUseCase.invoke(id, keterangan, file)

    override fun editFile(
        keterangan: RequestBody,
        file: MultipartBody.Part,
        id_file: RequestBody
    ): Observable<EditFileLampiranResponse> =
        editFileLampiranUseCase.invoke(keterangan, file, id_file)

    override fun removeFile(id_file: String): Observable<DeleteFileLampiranResponse> =
        removeFileLampiranUseCase.invoke(id_file)
}