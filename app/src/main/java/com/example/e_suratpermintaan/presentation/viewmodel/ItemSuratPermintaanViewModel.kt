package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.ItemSuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.*
import io.reactivex.rxjava3.core.Observable

class ItemSuratPermintaanViewModel(
    private val addItemSuratPermintaanUseCase: AddItemSuratPermintaanUseCase,
    private val removeItemSuratPermintaanUseCase: RemoveItemSuratPermintaanUseCase,
    private val editItemSuratPermintaanUseCase: EditItemSuratPermintaanUseCase,
    private val readDetailItemSuratPermintaanUseCase: ReadDetailItemSuratPermintaanUseCase,
    private val penugasanItemUseCase: SetPenugasanItemUseCase,
    private val processItemSuratPermintaanUseCase: ProcessItemSuratPermintaanUseCase,
    private val unProcessItemSuratPermintaanUseCase: UnProcessItemSuratPermintaanUseCase,
    private val rejectItemSuratPermintaanUseCase: RejectItemSuratPermintaanUseCase,
    private val rollbackItemSuratPermintaanUseCase: RollbackItemSuratPermintaanUseCase
) : ViewModel(), ItemSuratPermintaanDataSource {

    override fun addItem(createItemSP: CreateItemSP): Observable<CreateItemSPResponse> =
        addItemSuratPermintaanUseCase.invoke(createItemSP)

    override fun removeItem(id: String): Observable<DeleteItemSPResponse> =
        removeItemSuratPermintaanUseCase.invoke(id)

    override fun editItem(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse> =
        editItemSuratPermintaanUseCase.invoke(updateItemSP)


    override fun readDetailItem(id: String): Observable<DetailItemSPResponse> =
        readDetailItemSuratPermintaanUseCase.invoke(id)

    override fun setPenugasanItem(penugasanItemSP: PenugasanItemSP): Observable<PenugasanItemSPResponse> =
        penugasanItemUseCase.invoke(penugasanItemSP)

    override fun processItem(
        idSp: String,
        idItem: String,
        idUser: String
    ): Observable<ProcessItemSPResponse> =
        processItemSuratPermintaanUseCase.invoke(idSp, idItem, idUser)

    override fun unProcessItem(
        idSp: String,
        idItem: String,
        idUser: String
    ): Observable<ProcessItemSPResponse> =
        unProcessItemSuratPermintaanUseCase.invoke(idSp, idItem, idUser)

    override fun rejectItem(
        idUser: String,
        idSp: String,
        idItem: String,
        note: String
    ): Observable<RejectItemSPResponse> =
        rejectItemSuratPermintaanUseCase.invoke(idUser, idSp, idItem, note)

    override fun rollbackItem(
        idUser: String,
        idSp: String,
        idItem: String
    ): Observable<RollbackItemSPResponse> =
        rollbackItemSuratPermintaanUseCase.invoke(idUser, idSp, idItem)

}