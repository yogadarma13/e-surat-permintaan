package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.NotifikasiDataSource
import com.e_suratpermintaan.core.domain.entities.responses.NotifikasiResponse
import com.e_suratpermintaan.core.domain.entities.responses.ReadNotifikasiResponse
import com.e_suratpermintaan.core.usecases.notifikasi.GetNotifikasiListUseCase
import com.e_suratpermintaan.core.usecases.notifikasi.ReadNotifikasiUseCase
import io.reactivex.rxjava3.core.Observable

class NotifikasiViewModel(
    private val getNotifikasiListUseCase: GetNotifikasiListUseCase,
    private val readNotifikasiUseCase: ReadNotifikasiUseCase
) : ViewModel(), NotifikasiDataSource {
    override fun getNotifikasiList(id_user: String): Observable<NotifikasiResponse> =
        getNotifikasiListUseCase.invoke(id_user)

    override fun readNotifikasi(id_user: String, id: String): Observable<ReadNotifikasiResponse> =
        readNotifikasiUseCase.invoke(id_user, id)

}