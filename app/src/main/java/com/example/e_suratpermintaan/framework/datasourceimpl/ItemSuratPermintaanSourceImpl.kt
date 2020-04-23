package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.ItemSuratPermintaanDataSource
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DeleteItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.DetailItemSPResponse
import com.e_suratpermintaan.core.domain.entities.responses.EditItemSPResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class ItemSuratPermintaanSourceImpl(private val networkApi: NetworkApi) :
    ItemSuratPermintaanDataSource {
    override fun addItem(
        kode: String,
        kode_pekerjaan: String,
        id_barang: String,
        id_satuan: String,
        qty: String,
        fungsi: String,
        target: String,
        keterangan: String,
        kapasitas: String,
        merk: String,
        waktu_pemakaian: String,
        waktu_pelaksanaan: String,
        persyaratan: ArrayList<String>,
        id_user: String
    ): Observable<CreateItemSPResponse> =
        networkApi.createItemSP(
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
        networkApi.deleteItemSP(id)

    override fun editItem(
        kode: String,
        kode_pekerjaan: String,
        id_barang: String,
        id_satuan: String,
        qty: String,
        fungsi: String,
        target: String,
        keterangan: String,
        kapasitas: String,
        merk: String,
        waktu_pemakaian: String,
        waktu_pelaksanaan: String,
        persyaratan: ArrayList<String>,
        id_user: String,
        id_sp: String
    ): Observable<EditItemSPResponse> =
        networkApi.updateItemSP(
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
            id_user,
            id_sp
        )


    override fun readDetailItem(id: String): Observable<DetailItemSPResponse> =
        networkApi.detailItemSP(id)
}