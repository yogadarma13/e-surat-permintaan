package com.e_suratpermintaan.core.usecases.suratpermintaan

import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class ReadMyDataSuratPermintaanUseCase(
    private val suratPermintaanRepository: SuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(
        id_user: String,
//        proyek: String,
//        status_permintaan: String,
//        jenis_permintaan: String,
//        jenis_data: String
    ): Observable<MyDataResponse> =
        suratPermintaanRepository.readMyData(
            id_user,
//            proyek,
//            status_permintaan,
//            jenis_permintaan,
//            jenis_data
        )
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}