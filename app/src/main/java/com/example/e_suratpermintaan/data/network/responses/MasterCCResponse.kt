package com.example.e_suratpermintaan.data.network.responses

import com.example.e_suratpermintaan.data.network.responses.data_response.DataMasterCC
import com.google.gson.annotations.SerializedName

data class MasterCCResponse(

    @field:SerializedName("data")
	val data: List<DataMasterCC?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)