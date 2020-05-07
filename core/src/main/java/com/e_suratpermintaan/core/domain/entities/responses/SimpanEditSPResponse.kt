package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class SimpanEditSPResponse(

	@field:SerializedName("api_code")
	val apiCode: String? = null,

	@field:SerializedName("data")
	val data: DataEdit? = null,

	@field:SerializedName("data_sp")
	val dataSp: DataSp? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataSp(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("log")
	val log: String? = null
)

data class DataEdit(

	@field:SerializedName("id_sp")
	val idSp: String? = null
)
