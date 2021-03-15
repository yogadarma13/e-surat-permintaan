package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import com.e_suratpermintaan.core.domain.entities.responses.NotifItem
import com.example.e_suratpermintaan.databinding.NotifikasiUnreadListBinding
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class NotifikasiViewHolder(private val binding: NotifikasiUnreadListBinding) :
    BaseViewHolder(binding.root) {

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as NotifItem

        binding.root.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        binding.tvKeteranganNotifUnread.text = data.keterangan
        binding.tvKodeSpNotifUnread.text = data.kodeSp
        binding.tvDateNotifUnread.text = data.tanggal
    }
}