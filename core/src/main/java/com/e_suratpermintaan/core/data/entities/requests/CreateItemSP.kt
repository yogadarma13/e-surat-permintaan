package com.e_suratpermintaan.core.data.entities.requests

import com.google.gson.annotations.SerializedName

data class CreateItemSP(
    val kode: String?,
    val kode_pekerjaan: String?,
    val id_barang: String?,
    val id_satuan: String?,
    val qty: String?,
    val fungsi: String?,
    val target: String?,
    val keterangan: String?,
    val kapasitas: String?,
    val merk: String?,
    val waktu_pemakaian: String?,
    val waktu_pelaksanaan: String?,

    @field:SerializedName("persyaratan[]")
    val persyaratan: ArrayList<String?>?,

    val id_user: String?
)