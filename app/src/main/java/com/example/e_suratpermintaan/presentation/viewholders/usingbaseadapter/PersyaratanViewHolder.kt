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

        rootView.setOnClickListener {
            if (rootView.checkbox.isChecked){
                rootView.checkbox.isChecked = false
                data.status = "unchecked"
            } else {
                rootView.checkbox.isChecked = true
                data.status = "checked"
            }
             // rootView.checkbox.text = "${data.nama} ${data.status}"

            listener(data, ROOTVIEW)
        }

        rootView.checkbox.setOnClickListener {
            if (rootView.checkbox.isChecked){
                data.status = "checked"
            } else {
                data.status = "unchecked"
            }
             // rootView.checkbox.text = "${data.nama} ${data.status}"
        }
    }

}