package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class NotifikasiResponse(

	@field:SerializedName("data")
	val data: List<DataNotifikasi?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class NotifItem(

	@field:SerializedName("kode_sp")
	val kodeSp: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("id_sp")
	val idSp: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null
)

data class DataNotifikasi(

	@field:SerializedName("notif")
	val notif: List<NotifItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null
)
