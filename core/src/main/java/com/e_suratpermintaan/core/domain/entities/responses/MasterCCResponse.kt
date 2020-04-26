package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterCCResponse(

	@field:SerializedName("data")
	val data: List<DataMasterCC?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataMasterCC(

	@field:SerializedName("uom")
	val uom: String? = null,

	@field:SerializedName("kode_costcontrol")
	val kodeCostcontrol: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kode_kategori")
	val kodeKategori: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("created_by")
	val createdBy: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)
{
	override fun toString(): String {
		return "$kodeCostcontrol $deskripsi"
	}
}

