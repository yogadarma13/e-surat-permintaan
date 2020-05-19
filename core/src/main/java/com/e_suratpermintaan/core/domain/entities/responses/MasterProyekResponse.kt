package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterProyekResponse(

	@field:SerializedName("data")
	val data: List<DataMasterProyek?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataMasterProyek(

	@field:SerializedName("option")
	val option: String? = null,

	@field:SerializedName("value")
	val value: String? = null
) {
	override fun toString(): String = option.toString()
}
