package com.example.e_suratpermintaan.data.network.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataEditSP(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("log")
	val log: String? = null
)