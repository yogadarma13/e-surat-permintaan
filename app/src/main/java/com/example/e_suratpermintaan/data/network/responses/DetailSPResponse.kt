package com.example.e_suratpermintaan.data.network.responses

import com.example.e_suratpermintaan.data.network.responses.data_response.DataDetailSP
import com.google.gson.annotations.SerializedName

data class DetailSPResponse(

    @field:SerializedName("data")
	val data: List<DataDetailSP?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)