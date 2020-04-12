package com.example.e_suratpermintaan.data.network.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataAjukanSP(

	@field:SerializedName("status_permintaan")
	val statusPermintaan: Int? = null
)