package com.example.e_suratpermintaan.data.network.responses

import com.example.e_suratpermintaan.data.network.responses.data_response.DataMasterPersyaratan
import com.google.gson.annotations.SerializedName

data class MasterPersyaratanResponse(

    @field:SerializedName("data")
	val data: List<DataMasterPersyaratan?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)