package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class CreateItemSP(

    @field:SerializedName("kode")
    val kode: String,

    @field:SerializedName("kode_pekerjaan")
    val kode_pekerjaan: String,

    @field:SerializedName("id_barang")
    val id_barang: String,

    @field:SerializedName("id_satuan")
    val id_satuan: String,

    @field:SerializedName("qty")
    val qty: String,

    @field:SerializedName("fungsi")
    val fungsi: String,

    @field:SerializedName("target")
    val target: String,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("kapasitas")
    val kapasitas: String,

    @field:SerializedName("merk")
    val merk: String,

    @field:SerializedName("waktu_pemakaian")
    val waktu_pemakaian: String,

    @field:SerializedName("waktu_pelaksanaan")
    val waktu_pelaksanaan: String,

    @field:SerializedName("persyaratan")
    val persyaratan: ArrayList<String>,

    @field:SerializedName("id_user")
    val id_user: String
)