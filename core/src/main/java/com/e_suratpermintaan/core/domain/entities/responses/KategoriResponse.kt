package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class KategoriResponse(

	@field:SerializedName("data")
	val data: List<DataKategori?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataKategori(

	@field:SerializedName("kategori")
	val kategori: String? = null
)
