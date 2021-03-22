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

data class DataDetailSP(

	@field:SerializedName("input_qty")
	val inputQty: Int? = null,

	@field:SerializedName("tombol_edit")
	val tombolEdit: Int? = null,

	@field:SerializedName("tombol_hapus")
	val tombolHapus: Int? = null,

	@field:SerializedName("input_merk")
	val inputMerk: Int? = null,

	@field:SerializedName("input_fungsi")
	val inputFungsi: Int? = null,

	@field:SerializedName("tombol_simpan")
	val tombolSimpan: Int? = null,

	@field:SerializedName("tombol_print_by_process")
	val tombolPrintByProcess: Int? = null,

	@field:SerializedName("input_kapasitas")
	val inputKapasitas: Int? = null,

	@field:SerializedName("ttd_cc")
	val ttdCc: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tanggal_pengajuan")
	val tanggalPengajuan: String? = null,

	@field:SerializedName("tombol_tambah_file")
	val tombolTambahFile: Int? = null,

	@field:SerializedName("id_proyek")
	val idProyek: String? = null,

	@field:SerializedName("input_target")
	val inputTarget: Int? = null,

	@field:SerializedName("input_persyaratan")
	val inputPersyaratan: Int? = null,

	@field:SerializedName("status_permintaan")
	val statusPermintaan: String? = null,

	@field:SerializedName("ttd_pd")
	val ttdPd: String? = null,

	@field:SerializedName("tombol_ajukan")
	val tombolAjukan: Int? = null,

	@field:SerializedName("tombol_tambah_item")
	val tombolTambahItem: Int? = null,

	@field:SerializedName("qty_max")
	val qtyMax: Int? = null,

	@field:SerializedName("input_id_barang")
	val inputIdBarang: Int? = null,

	@field:SerializedName("ttd_pm")
	val ttdPm: String? = null,

	@field:SerializedName("id_pic")
	val idPic: String? = null,

	@field:SerializedName("ttd_lp")
	val ttdLp: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsDetailSP?>? = null,

	@field:SerializedName("tombol_tolak")
	val tombolTolak: Int? = null,

	@field:SerializedName("input_kepada")
	val inputKepada: Int? = null,

	@field:SerializedName("tombol_cetak")
	val tombolCetak: Int? = null,

	@field:SerializedName("file_proyek")
	val fileProyek: String? = null,

	@field:SerializedName("input_waktu_pemakaian")
	val inputWaktuPemakaian: Int? = null,

	@field:SerializedName("id_status")
	val idStatus: String? = null,

	@field:SerializedName("file_lampiran")
	val fileLampiran: List<FileLampiranDetailSP?>? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("id_pm")
	val idPm: String? = null,

	@field:SerializedName("input_id_satuan")
	val inputIdSatuan: Int? = null,

	@field:SerializedName("ttd_pic")
	val ttdPic: String? = null,

	@field:SerializedName("input_waktu_pelaksanaan")
	val inputWaktuPelaksanaan: Int? = null,

	@field:SerializedName("nama_proyek")
	val namaProyek: String? = null,

	@field:SerializedName("id_lp")
	val idLp: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("input_kode_pekerjaan")
	val inputKodePekerjaan: Int? = null,

	@field:SerializedName("id_pd")
	val idPd: String? = null,

	@field:SerializedName("nama_lokasi")
	val namaLokasi: String? = null,

	@field:SerializedName("input_keterangan")
	val inputKeterangan: Int? = null,

	@field:SerializedName("input_penugasan")
	val inputPenugasan: Int? = null,

	@field:SerializedName("id_cc")
	val idCc: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("tombol_terima")
	val tombolTerima: Int? = null,

	@field:SerializedName("tombol_batalkan")
	val tombolBatalkan: Int? = null
)

data class ItemsDetailSP(

	@field:SerializedName("keterangan")
	val keterangan: List<KeteranganItemsDetailSP?>? = null,

	@field:SerializedName("process_by")
	val processBy: String? = null,

	@field:SerializedName("waktu_pemakaian")
	val waktuPemakaian: String? = null,

	@field:SerializedName("tombol_edit_item")
	val tombolEditItem: Int? = null,

	@field:SerializedName("tombol_process")
	val tombolProcess: Int? = null,

	@field:SerializedName("kepada")
	val kepada: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("kapasitas")
	val kapasitas: String? = null,

	@field:SerializedName("tombol_un_process")
	val tombolUnProcess: Int? = null,

	@field:SerializedName("persyaratan")
	val persyaratan: List<PersyaratanItemDetailSP?>? = null,

	@field:SerializedName("id_satuan")
	val idSatuan: String? = null,

	@field:SerializedName("merk")
	val merk: String? = null,

	@field:SerializedName("tombol_rollback_item")
	val tombolRollbackItem: Int? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("waktu_pelaksanaan")
	val waktuPelaksanaan: String? = null,

	@field:SerializedName("fungsi")
	val fungsi: String? = null,

	@field:SerializedName("target")
	val target: String? = null,

	@field:SerializedName("tombol_penugasan")
	val tombolPenugasan: Int? = null,

	@field:SerializedName("id_barang")
	val idBarang: String? = null,

	@field:SerializedName("tombol_tolak_item")
	val tombolTolakItem: Int? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("status_pd")
	val statusPd: String? = null,

	@field:SerializedName("tombol_hapus_item")
	val tombolHapusItem: Int? = null,

	@field:SerializedName("kode_pekerjaan")
	val kodePekerjaan: String? = null,

	@field:SerializedName("penugasan")
	val penugasan: String? = null
)

data class FileLampiranDetailSP(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tombol_edit_file")
	val tombolEditFile: Int? = null,

	@field:SerializedName("tombol_hapus_file")
	val tombolHapusFile: Int? = null,

	@field:SerializedName("dir")
	val dir: String? = null,

	@field:SerializedName("id_file")
	val idFile: String? = null
)

data class KeteranganItemsDetailSP(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("role_user")
	val roleUser: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("nama_user")
	val namaUser: String? = null
)

data class PersyaratanItemDetailSP(

	@field:SerializedName("persyaratan")
	val persyaratan: String? = null
)
