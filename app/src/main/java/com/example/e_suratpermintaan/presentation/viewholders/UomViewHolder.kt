package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterUOM
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.simple_item_row.view.*

class UomViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(item: Any?) {
        val data = item as DataMasterUOM

        rootView.textView.text = data.nama
    }

}