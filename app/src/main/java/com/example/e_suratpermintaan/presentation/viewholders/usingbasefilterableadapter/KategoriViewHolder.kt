package com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataKategori
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_simple_row.view.*

class KategoriViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataKategori

        rootView.setOnClickListener {
            listener(data, ROOTVIEW)
        }

        rootView.textView.text = data.kategori
    }

}