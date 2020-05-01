package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.FileLampiranDetailSP
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_file_lampiran_row.view.*

class FileSuratPermintaanViewHolder(private val rootView: View) :
    BaseViewHolder(rootView) {

    companion object {
        const val BTN_FILE = "btnFile"
    }

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as FileLampiranDetailSP

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        rootView.btnFile.setOnClickListener {
            listener.invoke(data, BTN_FILE)
        }

        rootView.tvKeteranganFile.text = data.keterangan
        rootView.expandableIcon.visibility = View.GONE
    }
}