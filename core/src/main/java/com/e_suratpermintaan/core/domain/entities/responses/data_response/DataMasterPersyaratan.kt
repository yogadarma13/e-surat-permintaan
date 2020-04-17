package com.e_suratpermintaan.core.domain.entities.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataMasterPersyaratan(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)