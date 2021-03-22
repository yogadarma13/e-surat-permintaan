package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable

interface ItemSuratPermintaanDataSource {
    fun addItem(createItemSP: CreateItemSP): Observable<CreateItemSPResponse>

    fun removeItem(id: String): Observable<DeleteItemSPResponse>

    fun editItem(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse>

    fun readDetailItem(id: String): Observable<DetailItemSPResponse>

    fun setPenugasanItem(penugasanItemSP: PenugasanItemSP): Observable<PenugasanItemSPResponse>

    fun processItem(idSp: String, idItem: String, idUser: String): Observable<ProcessItemSPResponse>

    fun unProcessItem(
        idSp: String,
        idItem: String,
        idUser: String
    ): Observable<ProcessItemSPResponse>

    fun rejectItem(
        idUser: String,
        idSp: String,
        idItem: String,
        note: String
    ): Observable<RejectItemSPResponse>

    fun rollbackItem(
        idUser: String,
        idSp: String,
        idItem: String
    ): Observable<RollbackItemSPResponse>
}