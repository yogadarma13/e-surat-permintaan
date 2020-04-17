package com.e_suratpermintaan.core.domain.entities.responses

import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataCreateSP
import com.google.gson.annotations.SerializedName

data class CreateSPResponse(

    @field:SerializedName("api_code")
	val apiCode: String? = null,

    @field:SerializedName("data")
	val dataCreateSP: DataCreateSP? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)