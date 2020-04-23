package com.e_suratpermintaan.core.usecases.itemsuratpermintaan

import com.e_suratpermintaan.core.data.repository.ItemSuratPermintaanRepository
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable

class AddItemSuratPermintaanUseCase(
    private val itemSuratPermintaanRepository: ItemSuratPermintaanRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun invoke(
        kode: String?,
        kode_pekerjaan: String?,
        id_barang: String?,
        id_satuan: String?,
        qty: String?,
        fungsi: String?,
        target: String?,
        keterangan: String?,
        kapasitas: String?,
        merk: String?,
        waktu_pemakaian: String?,
        waktu_pelaksanaan: String?,
        persyaratan: ArrayList<String?>?,
        id_user: String?
    ): Observable<CreateItemSPResponse> =
        itemSuratPermintaanRepository.addItem(
            kode,
            kode_pekerjaan,
            id_barang,
            id_satuan,
            qty,
            fungsi,
            target,
            keterangan,
            kapasitas,
            merk,
            waktu_pemakaian,
            waktu_pelaksanaan,
            persyaratan,
            id_user
        )
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}