package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.FileLampiranDataSource
import com.e_suratpermintaan.core.domain.entities.responses.CreateFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteFileLampiranResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditFileLampiranResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FileLampiranSourceImpl(private val networkApi: NetworkApi) : FileLampiranDataSource {
    override fun addFile(
        id: RequestBody,
        keterangan: RequestBody,
        file: MultipartBody.Part
    ): Observable<CreateFileLampiranResponse> =
        networkApi.createFileLampiran(id, keterangan, file)

    override fun editFile(
        keterangan: RequestBody,
        file: MultipartBody.Part,
        id_file: RequestBody
    ): Observable<EditFileLampiranResponse> =
        networkApi.updateFileLampiran(keterangan, file, id_file)

    override fun removeFile(id_file: String): Observable<DeleteFileLampiranResponse> =
        networkApi.deleteFileLampiran(id_file)
}