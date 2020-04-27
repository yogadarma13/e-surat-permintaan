package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataAll
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_surat_permintaan_row.view.*

class DataAllViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(item: Any?) {
        val data = item as DataAll

        rootView.tvKode.text = data.kode
        rootView.tvTanggalPengajuan.text = data.tanggalPengajuan
        rootView.tvStatusPermintaan.text = data.statusPermintaan
    }

}