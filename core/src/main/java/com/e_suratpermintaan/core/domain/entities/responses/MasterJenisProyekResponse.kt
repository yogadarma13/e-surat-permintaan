package com.e_suratpermintaan.core.domain.entities.responses

import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMasterJenisProyek
import com.google.gson.annotations.SerializedName

data class MasterJenisProyekResponse(

    @field:SerializedName("data")
	val data: List<DataMasterJenisProyek?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)