package com.e_suratpermintaan.core.domain.entities.responses

import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataDetailItemSP
import com.google.gson.annotations.SerializedName

data class DetailItemSPResponse(

    @field:SerializedName("data")
	val data: List<DataDetailItemSP?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)