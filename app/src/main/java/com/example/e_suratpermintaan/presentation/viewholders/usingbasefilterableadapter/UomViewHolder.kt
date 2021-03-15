package com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter

import com.e_suratpermintaan.core.domain.entities.responses.DataMasterUOM
import com.example.e_suratpermintaan.databinding.ItemSimpleRowBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class UomViewHolder(private val binding: ItemSimpleRowBinding) : BaseViewHolder(binding.root) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataMasterUOM

        binding.root.setOnClickListener {
            listener(data, ROOTVIEW)
        }

        binding.textView.text = data.nama
    }

}