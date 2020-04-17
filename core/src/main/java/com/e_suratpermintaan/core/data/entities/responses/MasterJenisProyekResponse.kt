package com.e_suratpermintaan.core.data.entities.responses

import com.e_suratpermintaan.core.data.entities.responses.data_response.DataMaterJenisProyek
import com.google.gson.annotations.SerializedName

data class MasterJenisProyekResponse(

    @field:SerializedName("data")
	val data: List<DataMaterJenisProyek?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)