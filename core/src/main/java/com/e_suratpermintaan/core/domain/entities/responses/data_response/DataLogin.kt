package com.e_suratpermintaan.core.domain.entities.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataLogin(

	@field:SerializedName("ttd")
	val ttd: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("role_id")
	val roleId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)