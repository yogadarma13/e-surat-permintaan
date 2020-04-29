package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class DetailItemSPResponse(

    @field:SerializedName("data")
    val data: List<DataDetailItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class DataDetailItem(

    @field:SerializedName("id_satuan")
    val idSatuan: String? = null,

    @field:SerializedName("keterangan")
    val keterangan: String? = null,

    @field:SerializedName("merk")
    val merk: String? = null,

    @field:SerializedName("waktu_pelaksanaan")
    val waktuPelaksanaan: String? = null,

    @field:SerializedName("fungsi")
    val fungsi: String? = null,

    @field:SerializedName("waktu_pemakaian")
    val waktuPemakaian: String? = null,

    @field:SerializedName("target")
    val target: String? = null,

    @field:SerializedName("id_barang")
    val idBarang: String? = null,

    @field:SerializedName("qty")
    val qty: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("kode_pekerjaan")
    val kodePekerjaan: String? = null,

    @field:SerializedName("kapasitas")
    val kapasitas: String? = null,

    @field:SerializedName("persyaratan")
    val persyaratan: List<PersyaratanDetailItem?>? = null

)

data class PersyaratanDetailItem(

    @field:SerializedName("persyaratan")
    val persyaratan: String? = null
)
