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

data class DataNotifikasi(

	@field:SerializedName("read")
	val read: Any? = null,

	@field:SerializedName("unread")
	val unread: List<UnreadItem?>? = null,

	@field:SerializedName("count_unread")
	val countUnread: Int? = null
)

data class UnreadItem(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("tombol_read")
	val tombolRead: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("user")
	val user: String? = null
)
