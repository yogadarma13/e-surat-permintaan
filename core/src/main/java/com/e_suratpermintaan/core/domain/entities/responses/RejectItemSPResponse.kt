package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class RejectItemSPResponse(

	@field:SerializedName("data")
	val data: DataRejectItem? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataRejectItem(

	@field:SerializedName("log")
	val log: String? = null,

	@field:SerializedName("tgl_selesai")
	val tglSelesai: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("id_pm")
	val idPm: String? = null,

	@field:SerializedName("ttd_cc")
	val ttdCc: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("ttd_pic")
	val ttdPic: String? = null,

	@field:SerializedName("id_lp")
	val idLp: String? = null,

	@field:SerializedName("id_proyek")
	val idProyek: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: String? = null,

	@field:SerializedName("ttd_pd")
	val ttdPd: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("id_pd")
	val idPd: String? = null,

	@field:SerializedName("id_cc")
	val idCc: String? = null,

	@field:SerializedName("log_status")
	val logStatus: String? = null,

	@field:SerializedName("lokasi")
	val lokasi: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("ttd_pm")
	val ttdPm: String? = null,

	@field:SerializedName("id_pic")
	val idPic: String? = null,

	@field:SerializedName("ttd_lp")
	val ttdLp: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
