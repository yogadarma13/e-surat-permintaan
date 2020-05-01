package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterStatusPermintaanResponse(

	@field:SerializedName("data")
	val data: List<DataMasterStatusPermintaan?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataMasterStatusPermintaan(

    @field:SerializedName("option")
    val option: String? = null,

    @field:SerializedName("value")
    val value: String? = null
) {
	override fun toString(): String = option.toString()
}