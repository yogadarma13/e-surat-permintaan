package com.example.e_suratpermintaan.data.network.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse(

	@field:SerializedName("api_code")
	val apiCode: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)