package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class CreateSPResponse(

	@field:SerializedName("api_code")
	val apiCode: String? = null,

	@field:SerializedName("data")
	val data: DataCreateSP? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("id_sp")
	val idSp: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataCreateSP(

	@field:SerializedName("id_proyek")
	val idProyek: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: String? = null,

	@field:SerializedName("id_pic")
	val idPic: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
