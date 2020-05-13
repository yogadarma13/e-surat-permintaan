package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.NotifItem
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.notifikasi_unread_list.view.*

class NotifikasiViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as NotifItem

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        rootView.tvKeteranganNotifUnread.text =  data.keterangan
        rootView.tvKodeSpNotifUnread.text = data.kodeSp
        rootView.tvDateNotifUnread.text = data.tanggal
    }
}