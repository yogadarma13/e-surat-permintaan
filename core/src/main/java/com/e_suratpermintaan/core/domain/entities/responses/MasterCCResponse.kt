package com.e_suratpermintaan.core.domain.entities.responses

import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMasterCC
import com.google.gson.annotations.SerializedName

data class MasterCCResponse(

    @field:SerializedName("data")
	val data: List<DataMasterCC?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)