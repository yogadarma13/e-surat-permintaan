package com.example.e_suratpermintaan.data.network.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("api_code")
	val apiCode: String? = null,

	@field:SerializedName("data")
	val dataLogin: DataLogin? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)