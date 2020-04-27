package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterCC
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.simple_item_row.view.*

class CCViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(item: Any?) {
        val data = item as DataMasterCC

        rootView.textView.text = data.kodeCostcontrol
    }

}