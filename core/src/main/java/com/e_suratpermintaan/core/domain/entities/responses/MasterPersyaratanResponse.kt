package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterPersyaratanResponse(

	@field:SerializedName("data")
	val data: List<DataMasterPersyaratan?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataMasterPersyaratan(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("value")
    val value: String? = null,

    @field:SerializedName("option")
    val option: String? = null,

    var isChecked: Boolean = false
)
