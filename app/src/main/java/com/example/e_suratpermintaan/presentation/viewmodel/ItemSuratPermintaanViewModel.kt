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
    override fun addItem(createItemSP: CreateItemSP): Observable<CreateItemSPResponse> =
        addItemSuratPermintaanUseCase.invoke(createItemSP)

    override fun removeItem(id: String): Observable<DeleteItemSPResponse> =
        removeItemSuratPermintaanUseCase.invoke(id)

    override fun editItem(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse> =
        editItemSuratPermintaanUseCase.invoke(updateItemSP)

    override fun readDetailItem(id: String): Observable<DetailItemSPResponse> =
        readDetailItemSuratPermintaanUseCase.invoke(id)
}