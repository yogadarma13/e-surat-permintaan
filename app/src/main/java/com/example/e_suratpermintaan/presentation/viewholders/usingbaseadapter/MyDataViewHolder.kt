package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.graphics.Color
import com.e_suratpermintaan.core.domain.entities.responses.DataSuratPermintaan
import com.example.e_suratpermintaan.databinding.ItemSuratPermintaanRowBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class MyDataViewHolder(private val binding: ItemSuratPermintaanRowBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataSuratPermintaan

        binding.root.setOnClickListener {
            listener(data, null)
        }

        binding.tvKode.text = data.kode
        binding.tvTanggalPengajuan.text = data.tanggalPengajuan
        binding.tvStatusPermintaan.text = data.statusPermintaan
        binding.tvJenis.text = data.jenis
        binding.tvProyek.text = data.namaProyek
        binding.tvPabrik.text = data.namaLokasi
        binding.tvDurasi.text = data.durasi
        binding.cardStatusPermintaan.setCardBackgroundColor(Color.parseColor(data.warna))
    }

}