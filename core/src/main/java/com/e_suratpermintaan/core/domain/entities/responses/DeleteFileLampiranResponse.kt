package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class DeleteFileLampiranResponse(

	@field:SerializedName("data")
	val data: DataDeleteFileLampiran? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataDeleteFileLampiran(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("mime")
	val mime: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("dir")
	val dir: String? = null,

	@field:SerializedName("table_id")
	val tableId: String? = null,

	@field:SerializedName("table")
	val table: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
