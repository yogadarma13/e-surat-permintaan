package com.e_suratpermintaan.core.data.entities.responses

import com.e_suratpermintaan.core.data.entities.responses.data_response.DataDeleteSP
import com.google.gson.annotations.SerializedName

data class DeleteSPResponse(

    @field:SerializedName("data")
	val dataDeleteSP: DataDeleteSP? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)