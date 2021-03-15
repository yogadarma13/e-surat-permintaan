package com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter

import com.e_suratpermintaan.core.domain.entities.responses.DataMasterCC
import com.example.e_suratpermintaan.databinding.ItemSimpleRowBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class JenisBarangViewHolder(private val binding: ItemSimpleRowBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataMasterCC

        binding.root.setOnClickListener {
            listener(data, ROOTVIEW)
        }

        binding.textView.text = data.deskripsi
    }

}