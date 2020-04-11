package com.example.e_suratpermintaan.data.network.responses

import com.google.gson.annotations.SerializedName

data class DataMaterJenisProyek(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)