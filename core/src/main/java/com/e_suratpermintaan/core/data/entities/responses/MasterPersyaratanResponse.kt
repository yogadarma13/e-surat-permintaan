package com.e_suratpermintaan.core.data.entities.responses

import com.e_suratpermintaan.core.data.entities.responses.data_response.DataMasterPersyaratan
import com.google.gson.annotations.SerializedName

data class MasterPersyaratanResponse(

    @field:SerializedName("data")
	val data: List<DataMasterPersyaratan?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)