package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.ItemSuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable

class ItemSuratPermintaanRepository(private val itemDataSource: ItemSuratPermintaanDataSource) :
    ItemSuratPermintaanDataSource {
    override fun addItem(createItemSP: CreateItemSP): Observable<CreateItemSPResponse> =
        itemDataSource.addItem(createItemSP)

    override fun removeItem(id: String): Observable<DeleteItemSPResponse> =
        itemDataSource.removeItem(id)

    override fun editItem(
        updateItemSP: UpdateItemSP
    ): Observable<EditItemSPResponse> =
        itemDataSource.editItem(
            updateItemSP
        )

    override fun readDetailItem(id: String): Observable<DetailItemSPResponse> =
        itemDataSource.readDetailItem(id)

    override fun setPenugasanItem(penugasanItemSP: PenugasanItemSP): Observable<PenugasanItemSPResponse> =
        itemDataSource.setPenugasanItem(penugasanItemSP)
}