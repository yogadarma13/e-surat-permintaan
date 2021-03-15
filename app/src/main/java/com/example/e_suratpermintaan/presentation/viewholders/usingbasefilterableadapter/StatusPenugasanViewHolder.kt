package com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter

import com.e_suratpermintaan.core.domain.entities.responses.DataMasterOption
import com.example.e_suratpermintaan.databinding.ItemSimpleRowBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class StatusPenugasanViewHolder(private val binding: ItemSimpleRowBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataMasterOption

        binding.root.setOnClickListener {
            listener(data, ROOTVIEW)
        }

        binding.textView.text = data.option
    }

}