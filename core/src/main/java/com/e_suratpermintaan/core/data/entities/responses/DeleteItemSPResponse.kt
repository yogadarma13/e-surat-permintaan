package com.e_suratpermintaan.core.data.entities.responses

import com.e_suratpermintaan.core.data.entities.responses.data_response.DataDeleteItemSP
import com.google.gson.annotations.SerializedName

data class DeleteItemSPResponse(

    @field:SerializedName("data")
	val dataDeleteItemSP: DataDeleteItemSP? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)