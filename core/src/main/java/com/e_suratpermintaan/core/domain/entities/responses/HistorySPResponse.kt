package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class HistorySPResponse(

	@field:SerializedName("data")
	val data: List<DataHistory?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataHistory(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("tombol_detail")
	val tombolDetail: Int? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("detail")
	val detail: List<DetailDataHistory?>? = null,

	@field:SerializedName("user")
	val user: String? = null
)

data class DetailDataHistory(

	@field:SerializedName("files")
	val files: Any? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsHistory?>? = null
)

data class ItemsHistory(

	@field:SerializedName("persyaratan")
	val persyaratan: Any? = null,

	@field:SerializedName("id_satuan")
	val idSatuan: Any? = null,

	@field:SerializedName("keterangan")
	val keterangan: Any? = null,

	@field:SerializedName("merk")
	val merk: Any? = null,

	@field:SerializedName("id_barang")
	val idBarang: Any? = null,

	@field:SerializedName("qty")
	val qty: Any? = null,

	@field:SerializedName("kode_pekerjaan")
	val kodePekerjaan: Any? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: Any? = null,

	@field:SerializedName("waktu_pelaksanaan")
	val waktuPelaksanaan: Any? = null,

	@field:SerializedName("fungsi")
	val fungsi: Any? = null,

	@field:SerializedName("waktu_pemakaian")
	val waktuPemakaian: Any? = null,

	@field:SerializedName("target")
	val target: Any? = null
)
