package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class EditSPResponse(

	@field:SerializedName("api_code")
	val apiCode: String? = null,

	@field:SerializedName("data")
	val data: DataEditSP? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataEditSP(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("log")
	val log: String? = null
)
