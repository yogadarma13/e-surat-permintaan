package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.CreateFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditFileLampiranResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface FileLampiranDataSource {

    fun addFile(id: RequestBody, keterangan: RequestBody, file: MultipartBody.Part): Observable<CreateFileLampiranResponse>

    fun editFile(keterangan: RequestBody, file: MultipartBody.Part, id_file: RequestBody): Observable<EditFileLampiranResponse>

    fun removeFile(id_file: String): Observable<DeleteFileLampiranResponse>
}