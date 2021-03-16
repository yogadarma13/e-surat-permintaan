package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import com.e_suratpermintaan.core.domain.entities.responses.DataMasterPersyaratan
import com.example.e_suratpermintaan.databinding.ItemSimpleCheckboxBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class PersyaratanViewHolder(private val binding: ItemSimpleCheckboxBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataMasterPersyaratan

        binding.checkbox.text = data.option

        binding.checkbox.isChecked = data.isChecked

        if (data.isChecked) {
            binding.checkbox.isChecked = true
        }

        binding.root.setOnClickListener {
            if (binding.checkbox.isChecked) {
                binding.checkbox.isChecked = false
                data.isChecked = false
            } else {
                binding.checkbox.isChecked = true
                data.isChecked = true
            }
            // binding.checkbox.text = "${data.nama} ${data.status}"

            listener(data, ROOTVIEW)
        }

        binding.checkbox.setOnClickListener {
            data.isChecked = binding.checkbox.isChecked
            // binding.checkbox.text = "${data.nama} ${data.status}"
        }
    }

}