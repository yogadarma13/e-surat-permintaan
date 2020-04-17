package com.e_suratpermintaan.core.data.entities.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataMasterCC(

	@field:SerializedName("uom")
	val uom: String? = null,

	@field:SerializedName("kode_costcontrol")
	val kodeCostcontrol: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kode_kategori")
	val kodeKategori: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)