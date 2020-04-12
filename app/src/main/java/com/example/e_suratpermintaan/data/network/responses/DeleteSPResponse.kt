package com.example.e_suratpermintaan.data.network.responses

import com.example.e_suratpermintaan.data.network.responses.data_response.DataDeleteSP
import com.google.gson.annotations.SerializedName

data class DeleteSPResponse(

	@field:SerializedName("data")
	val dataDeleteSP: DataDeleteSP? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)