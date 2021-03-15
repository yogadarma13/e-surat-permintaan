package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import com.e_suratpermintaan.core.domain.entities.responses.FilesDetailHistory
import com.example.e_suratpermintaan.databinding.FileDownloadItemBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class FileDownloadViewHolder(private val binding: FileDownloadItemBinding) :
    BaseViewHolder(binding.root) {
    companion object {
        const val BTN_FILE = "btn_file_history"
    }

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as FilesDetailHistory

        binding.root.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }
        binding.btnDownloadFile.setOnClickListener {
            listener.invoke(data, BTN_FILE)
        }
        binding.btnDownloadFile.text = data.keterangan

    }
}