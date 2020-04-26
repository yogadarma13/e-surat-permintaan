package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_surat_permintaan_row.view.*

class ItemSuratPermintaanViewHolder(private val rootView: View) :
    BaseViewHolder(rootView) {

    override fun bind(item: Any?) {
        val data = item as ItemsDetailSP

        rootView.tvKode.text = data.kodePekerjaan
        rootView.tvTanggalPengajuan.text = data.target
        rootView.tvStatusPermintaan.text = data.keterangan
    }

}