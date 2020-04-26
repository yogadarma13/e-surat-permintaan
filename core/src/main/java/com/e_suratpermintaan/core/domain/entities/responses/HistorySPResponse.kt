package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HistorySPResponse(

	@field:SerializedName("data")
	val data: List<DataHistory?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataHistory(

	@field:SerializedName("warna")
	val warna: String? = null,

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
	val detail: List<DetailHistory?>? = null,

	@field:SerializedName("user")
	val user: String? = null
)

data class DetailHistory(

	@field:SerializedName("files")
	val files: List<FilesDetailHistory?>? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsDetailHistory?>? = null
)

data class ItemsDetailHistory(

	@field:SerializedName("persyaratan")
	val persyaratan: List<Any?>? = null,

	@field:SerializedName("id_satuan")
	val idSatuan: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: List<KeteranganItem?>? = null,

	@field:SerializedName("merk")
	val merk: Any? = null,

	@field:SerializedName("id_barang")
	val idBarang: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("kode_pekerjaan")
	val kodePekerjaan: String? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: Any? = null,

	@field:SerializedName("waktu_pelaksanaan")
	val waktuPelaksanaan: Any? = null,

	@field:SerializedName("fungsi")
	val fungsi: String? = null,

	@field:SerializedName("waktu_pemakaian")
	val waktuPemakaian: Any? = null,

	@field:SerializedName("target")
	val target: String? = null
)

data class FilesDetailHistory(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("dir")
	val dir: String? = null
)

data class KeteranganItem(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("role_user")
	val roleUser: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("nama_user")
	val namaUser: String? = null
)
