package com.example.e_suratpermintaan.data.network.requests

data class UpdateItemSP (
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
    val persyaratan: List<String?>?,
    val id_user: String?,
    val id_sp: String?
)