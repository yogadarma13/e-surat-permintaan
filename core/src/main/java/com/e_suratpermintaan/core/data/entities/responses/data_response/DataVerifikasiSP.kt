package com.e_suratpermintaan.core.data.entities.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataVerifikasiSP(

	@field:SerializedName("id_pm")
	val idPm: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: Int? = null
)