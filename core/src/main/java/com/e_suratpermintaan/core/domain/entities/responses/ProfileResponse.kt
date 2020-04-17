package com.e_suratpermintaan.core.domain.entities.responses

import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataProfile
import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("data")
	val data: List<DataProfile?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)