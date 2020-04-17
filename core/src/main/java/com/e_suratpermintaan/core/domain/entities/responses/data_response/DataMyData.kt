package com.e_suratpermintaan.core.domain.entities.responses.data_response

import com.google.gson.annotations.SerializedName

data class DataMyData(

	@field:SerializedName("id_proyek")
	val idProyek: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: String? = null,

	@field:SerializedName("id_pd")
	val idPd: String? = null,

	@field:SerializedName("nama_lokasi")
	val namaLokasi: String? = null,

	@field:SerializedName("id_cc")
	val idCc: String? = null,

	@field:SerializedName("id_status")
	val idStatus: String? = null,

	@field:SerializedName("file_lampiran")
	val fileLampiran: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("id_pm")
	val idPm: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("id_pic")
	val idPic: String? = null,

	@field:SerializedName("tanggal_pengajuan")
	val tanggalPengajuan: String? = null,

	@field:SerializedName("nama_proyek")
	val namaProyek: String? = null,

	@field:SerializedName("id_lp")
	val idLp: String? = null
)