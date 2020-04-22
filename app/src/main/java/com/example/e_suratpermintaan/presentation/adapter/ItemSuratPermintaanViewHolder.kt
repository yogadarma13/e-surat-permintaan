package com.example.e_suratpermintaan.presentation.adapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.surat_permintaan_row.view.*

class ItemSuratPermintaanViewHolder(private val rootView: View) :
    BaseViewHolder<ItemsDetailSP>(rootView) {

    override fun bind(item: ItemsDetailSP?) {
        val data = item

        rootView.tvKode.text = data?.kodePekerjaan
        rootView.tvTanggalPengajuan.text = data?.qty
        rootView.tvStatusPermintaan.text = data?.keterangan
    }

}