package com.example.e_suratpermintaan.data.network.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataDeleteSP(

	@field:SerializedName("log")
	val log: String? = null,

	@field:SerializedName("id_proyek")
	val idProyek: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("id_pd")
	val idPd: String? = null,

	@field:SerializedName("id_cc")
	val idCc: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("log_status")
	val logStatus: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("id_pm")
	val idPm: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("id_pic")
	val idPic: String? = null,

	@field:SerializedName("id_lp")
	val idLp: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)