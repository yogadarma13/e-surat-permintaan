package com.example.e_suratpermintaan.data.network.responses

import com.google.gson.annotations.SerializedName

data class DetailItemSPResponse(

	@field:SerializedName("data")
	val data: List<DataDetailItemSP?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)