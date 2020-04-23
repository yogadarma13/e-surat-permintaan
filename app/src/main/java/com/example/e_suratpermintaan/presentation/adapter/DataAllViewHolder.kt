package com.example.e_suratpermintaan.presentation.adapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataAll
import com.e_suratpermintaan.core.domain.entities.responses.SuratPermintaan
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_surat_permintaan_row.view.*

class DataAllViewHolder(private val rootView: View) : BaseViewHolder<SuratPermintaan>(rootView) {

    override fun bind(item: SuratPermintaan?) {
        val data = item as DataAll

        rootView.tvKode.text = data.kode
        rootView.tvTanggalPengajuan.text = data.tanggalPengajuan
        rootView.tvStatusPermintaan.text = data.statusPermintaan
    }

}