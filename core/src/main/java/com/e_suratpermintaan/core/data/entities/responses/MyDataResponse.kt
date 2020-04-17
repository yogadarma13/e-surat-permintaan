package com.e_suratpermintaan.core.data.entities.responses

import com.e_suratpermintaan.core.data.entities.responses.data_response.DataMyData
import com.google.gson.annotations.SerializedName

data class MyDataResponse(

    @field:SerializedName("data")
	val data: List<DataMyData?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)