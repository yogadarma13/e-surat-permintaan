package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("api_code")
	val apiCode: String? = null,

	@field:SerializedName("data")
	val data: DataEditProfile? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataEditProfile(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("ttd")
	val ttd: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)
