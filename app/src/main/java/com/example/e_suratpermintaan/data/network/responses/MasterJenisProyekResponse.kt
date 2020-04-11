package com.example.e_suratpermintaan.data.network.responses

import com.google.gson.annotations.SerializedName

data class MasterJenisProyekResponse(

    @field:SerializedName("data")
	val data: List<DataMaterJenisProyek?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)