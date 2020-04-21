package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: List<DataProfile?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataProfile(

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("role_id")
	val roleId: String? = null,

	@field:SerializedName("ttd")
	val ttd: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("jenis")
	val jenis: List<JenisItemProfile?>? = null,

	@field:SerializedName("nama_role")
	val namaRole: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

data class JenisItemProfile(

	@field:SerializedName("jenis")
	val jenis: String? = null
)
