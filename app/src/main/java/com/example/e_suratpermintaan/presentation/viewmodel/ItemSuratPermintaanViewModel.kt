package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.ItemSuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DetailItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditItemSPResponse
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.AddItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.EditItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.ReadDetailItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.RemoveItemSuratPermintaanUseCase
import io.reactivex.rxjava3.core.Observable

class ItemSuratPermintaanViewModel(
    private val addItemSuratPermintaanUseCase: AddItemSuratPermintaanUseCase,
    private val removeItemSuratPermintaanUseCase: RemoveItemSuratPermintaanUseCase,
    private val editItemSuratPermintaanUseCase: EditItemSuratPermintaanUseCase,
    private val readDetailItemSuratPermintaanUseCase: ReadDetailItemSuratPermintaanUseCase
) : ViewModel(), ItemSuratPermintaanDataSource {
    override fun addItem(
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
        addItemSuratPermintaanUseCase.invoke(
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

    override fun removeItem(id: String): Observable<DeleteItemSPResponse> =
        removeItemSuratPermintaanUseCase.invoke(id)

    override fun editItem(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse> =
        editItemSuratPermintaanUseCase.invoke(updateItemSP)

    override fun readDetailItem(id: String): Observable<DetailItemSPResponse> =
        readDetailItemSuratPermintaanUseCase.invoke(id)
}