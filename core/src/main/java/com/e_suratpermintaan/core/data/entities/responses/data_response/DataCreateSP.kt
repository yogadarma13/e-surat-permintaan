package com.e_suratpermintaan.core.data.entities.responses.data_response

import com.google.gson.annotations.SerializedName

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