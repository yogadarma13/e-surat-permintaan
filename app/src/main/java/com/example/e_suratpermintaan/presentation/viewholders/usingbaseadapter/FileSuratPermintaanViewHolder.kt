package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.FileLampiranDetailSP
import com.example.e_suratpermintaan.databinding.ItemFileLampiranRowBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class FileSuratPermintaanViewHolder(private val binding: ItemFileLampiranRowBinding) :
    BaseViewHolder(binding.root) {

    companion object {
        const val BTN_FILE = "btnFile"
    }

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as FileLampiranDetailSP

        binding.root.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        binding.btnFile.setOnClickListener {
            listener.invoke(data, BTN_FILE)
        }

        binding.tvKeteranganFile.text = data.keterangan
        binding.expandableIcon.visibility = View.GONE
    }
}