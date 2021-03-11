package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.ItemSuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class ItemSuratPermintaanSourceImpl(private val networkApi: NetworkApi) :
    ItemSuratPermintaanDataSource {
    override fun addItem(createItemSP: CreateItemSP): Observable<CreateItemSPResponse> =
        networkApi.createItemSP(createItemSP)

    override fun removeItem(id: String): Observable<DeleteItemSPResponse> =
        networkApi.deleteItemSP(id)

    override fun editItem(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse> =
        networkApi.updateItemSP(updateItemSP)

    override fun readDetailItem(id: String): Observable<DetailItemSPResponse> =
        networkApi.detailItemSP(id)

    override fun setPenugasanItem(penugasanItemSP: PenugasanItemSP): Observable<PenugasanItemSPResponse> =
        networkApi.setPenugasanItemSP(penugasanItemSP)

    override fun processItem(
        idSp: String,
        idItem: String,
        idUser: String
    ): Observable<ProcessItemSPResponse> = networkApi.processItemSP(idSp, idItem, idUser)

    override fun unProcessItem(
        idSp: String,
        idItem: String,
        idUser: String
    ): Observable<ProcessItemSPResponse> = networkApi.unProcessItemSP(idSp, idItem, idUser)
}