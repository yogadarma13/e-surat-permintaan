package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterJenisDataPermintaanResponse(

	@field:SerializedName("data")
	val data: List<DataMasterJenisDataPermintaan?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataMasterJenisDataPermintaan(

    @field:SerializedName("option")
    val option: String? = null,

    @field:SerializedName("value")
    val value: String? = null
){
	override fun toString(): String = option.toString()
}