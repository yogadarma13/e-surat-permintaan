package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataAll
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class DataAllViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as DataAll

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

//        rootView.tvKode.text = data.kode
//        rootView.tvTanggalPengajuan.text = data.tanggalPengajuan
//        rootView.tvStatusPermintaan.text = data.statusPermintaan
    }

}