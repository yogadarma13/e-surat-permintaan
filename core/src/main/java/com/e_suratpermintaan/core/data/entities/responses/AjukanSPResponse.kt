package com.e_suratpermintaan.core.data.entities.responses

import com.e_suratpermintaan.core.data.entities.responses.data_response.DataAjukanSP
import com.google.gson.annotations.SerializedName

data class AjukanSPResponse(

    @field:SerializedName("api_code")
	val apiCode: String? = null,

    @field:SerializedName("data")
	val dataAjukanSP: DataAjukanSP? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)