package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.FileLampiranDataSource
import com.e_suratpermintaan.core.domain.entities.responses.CreateFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditFileLampiranResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FileLampiranRepository(private val fileDataSource: FileLampiranDataSource) :
    FileLampiranDataSource {
    override fun addFile(
        id: RequestBody,
        keterangan: RequestBody,
        file: MultipartBody.Part
    ): Observable<CreateFileLampiranResponse> =
        fileDataSource.addFile(id, keterangan, file)

    override fun editFile(
        keterangan: RequestBody,
        file: MultipartBody.Part,
        id_file: RequestBody
    ): Observable<EditFileLampiranResponse> =
        fileDataSource.editFile(keterangan, file, id_file)

    override fun removeFile(id_file: String): Observable<DeleteFileLampiranResponse> =
        fileDataSource.removeFile(id_file)
}