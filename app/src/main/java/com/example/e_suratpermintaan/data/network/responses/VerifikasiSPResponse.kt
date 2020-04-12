package com.example.e_suratpermintaan.data.network.responses

import com.example.e_suratpermintaan.data.network.responses.data_response.DataVerifikasiSP
import com.google.gson.annotations.SerializedName

data class VerifikasiSPResponse(

    @field:SerializedName("api_code")
	val apiCode: String? = null,

    @field:SerializedName("data")
	val dataVerifikasiSP: DataVerifikasiSP? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)