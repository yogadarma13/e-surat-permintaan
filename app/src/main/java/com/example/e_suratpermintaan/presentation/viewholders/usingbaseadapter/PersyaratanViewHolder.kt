package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterPersyaratan
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_simple_checkbox.view.*

class PersyaratanViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataMasterPersyaratan

        rootView.checkbox.text = data.nama

        rootView.checkbox.isChecked = data.isChecked

        if (data.isChecked) {
            rootView.checkbox.isChecked = true
        }

        rootView.setOnClickListener {
            if (rootView.checkbox.isChecked) {
                rootView.checkbox.isChecked = false
                data.isChecked = false
            } else {
                rootView.checkbox.isChecked = true
                data.isChecked = true
            }
            // rootView.checkbox.text = "${data.nama} ${data.status}"

            listener(data, ROOTVIEW)
        }

        rootView.checkbox.setOnClickListener {
            data.isChecked = rootView.checkbox.isChecked
            // rootView.checkbox.text = "${data.nama} ${data.status}"
        }
    }

}