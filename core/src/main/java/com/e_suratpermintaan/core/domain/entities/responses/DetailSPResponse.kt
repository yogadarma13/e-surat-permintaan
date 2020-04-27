package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class DetailSPResponse(

	@field:SerializedName("data")
	val data: List<DataDetailSP?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ItemsDetailSP(

	@field:SerializedName("persyaratan")
	val persyaratan: List<PersyaratanItemDetailSP?>? = null,

	@field:SerializedName("id_satuan")
	val idSatuan: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: List<KeteranganDetailSP?>? = null,

	@field:SerializedName("merk")
	val merk: String? = null,

	@field:SerializedName("waktu_pelaksanaan")
	val waktuPelaksanaan: String? = null,

	@field:SerializedName("fungsi")
	val fungsi: String? = null,

	@field:SerializedName("waktu_pemakaian")
	val waktuPemakaian: String? = null,

	@field:SerializedName("tombol_edit_item")
	val tombolEditItem: Int? = null,

	@field:SerializedName("target")
	val target: String? = null,

	@field:SerializedName("tombol_tambah_item")
	val tombolTambahItem: Int? = null,

	@field:SerializedName("id_barang")
	val idBarang: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("tombol_hapus_item")
	val tombolHapusItem: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kode_pekerjaan")
	val kodePekerjaan: String? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: String? = null
)

data class DataDetailSP(

	@field:SerializedName("tombol_edit")
	val tombolEdit: Int? = null,

	@field:SerializedName("tombol_hapus")
	val tombolHapus: Int? = null,

	@field:SerializedName("tombol_cetak")
	val tombolCetak: Int? = null,

	@field:SerializedName("tombol_simpan_draft")
	val tombolSimpanDraft: Int? = null,

	@field:SerializedName("tombol_simpan")
	val tombolSimpan: Int? = null,

	@field:SerializedName("id_status")
	val idStatus: String? = null,

	@field:SerializedName("file_lampiran")
	val fileLampiran: List<FileLampiranDetailSP?>? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("id_pm")
	val idPm: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal_pengajuan")
	val tanggalPengajuan: String? = null,

	@field:SerializedName("nama_proyek")
	val namaProyek: String? = null,

	@field:SerializedName("id_lp")
	val idLp: String? = null,

	@field:SerializedName("id_proyek")
	val idProyek: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: String? = null,

	@field:SerializedName("id_pd")
	val idPd: String? = null,

	@field:SerializedName("tombol_ajukan")
	val tombolAjukan: Int? = null,

	@field:SerializedName("nama_lokasi")
	val namaLokasi: String? = null,

	@field:SerializedName("id_cc")
	val idCc: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("tombol_terima")
	val tombolTerima: Int? = null,

	@field:SerializedName("id_pic")
	val idPic: String? = null,

	@field:SerializedName("tombol_batalkan")
	val tombolBatalkan: Int? = null,

	@field:SerializedName("items")
	val items: List<ItemsDetailSP?>? = null,

	@field:SerializedName("tombol_tolak")
	val tombolTolak: Int? = null
)

data class KeteranganDetailSP(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("role_user")
	val roleUser: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("nama_user")
	val namaUser: String? = null
)

data class FileLampiranDetailSP(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("dir")
	val dir: String? = null
)

data class PersyaratanItemDetailSP(

	@field:SerializedName("persyaratan")
	val persyaratan: String? = null
)

