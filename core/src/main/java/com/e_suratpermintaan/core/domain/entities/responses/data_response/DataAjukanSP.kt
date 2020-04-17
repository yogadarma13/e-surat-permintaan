package com.e_suratpermintaan.core.domain.entities.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataAjukanSP(

	@field:SerializedName("status_permintaan")
	val statusPermintaan: Int? = null
)