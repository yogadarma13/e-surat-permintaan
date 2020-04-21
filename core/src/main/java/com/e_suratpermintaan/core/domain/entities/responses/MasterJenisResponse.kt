package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterJenisResponse(

	@field:SerializedName("data")
	val data: List<DataMasterJenis?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataMasterJenis(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) {
	override fun toString(): String = nama.toString()
}
