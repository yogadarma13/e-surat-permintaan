package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class DeleteItemSPResponse(

	@field:SerializedName("data")
	val data: DataDeleteItemSP? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataDeleteItemSP(

	@field:SerializedName("persyaratan")
	val persyaratan: String? = null,

	@field:SerializedName("id_satuan")
	val idSatuan: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("merk")
	val merk: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("waktu_pelaksanaan")
	val waktuPelaksanaan: String? = null,

	@field:SerializedName("fungsi")
	val fungsi: String? = null,

	@field:SerializedName("waktu_pemakaian")
	val waktuPemakaian: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("target")
	val target: String? = null,

	@field:SerializedName("kepada")
	val kepada: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id_barang")
	val idBarang: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kode_pekerjaan")
	val kodePekerjaan: String? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: String? = null,

	@field:SerializedName("penugasan")
	val penugasan: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
