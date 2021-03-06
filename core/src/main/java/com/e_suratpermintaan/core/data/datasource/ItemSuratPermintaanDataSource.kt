package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DetailItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditItemSPResponse
import io.reactivex.rxjava3.core.Observable

interface ItemSuratPermintaanDataSource {
    fun addItem(createItemSP: CreateItemSP): Observable<CreateItemSPResponse>

    fun removeItem(id: String): Observable<DeleteItemSPResponse>

    fun editItem(updateItemSP: UpdateItemSP): Observable<EditItemSPResponse>

    fun readDetailItem(id: String): Observable<DetailItemSPResponse>
}