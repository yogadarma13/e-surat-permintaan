package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataSuratPermintaan
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class DataAllViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataSuratPermintaan

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

    }

}