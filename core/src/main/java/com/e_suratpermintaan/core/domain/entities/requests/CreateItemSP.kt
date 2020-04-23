package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class CreateItemSP(

    @SerializedName("kode")
    val kode: String?,

    @SerializedName("kode_pekerjaan")
    val kode_pekerjaan: String?,

    @SerializedName("id_barang")
    val id_barang: String?,

    @SerializedName("id_satuan")
    val id_satuan: String?,

    @SerializedName("qty")
    val qty: String?,

    @SerializedName("fungsi")
    val fungsi: String?,

    @SerializedName("target")
    val target: String?,

    @SerializedName("keterangan")
    val keterangan: String?,

    @SerializedName("kapasitas")
    val kapasitas: String?,

    @SerializedName("merk")
    val merk: String?,

    @SerializedName("waktu_pemakaian")
    val waktu_pemakaian: String?,

    @SerializedName("waktu_pelaksanaan")
    val waktu_pelaksanaan: String?,

    @SerializedName("persyaratan[]")
    val persyaratan: ArrayList<String?>?,

    @SerializedName("id_user")
    val id_user: String?
)