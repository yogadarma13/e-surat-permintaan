package com.example.e_suratpermintaan.presentation.adapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.detail_history_item.view.*

class DetailHistoryViewHolder(private val rootView: View) :
    BaseViewHolder<ItemsDetailHistory>(rootView) {

    override fun bind(item: ItemsDetailHistory?) {
        val data = item

        rootView.tvKodeDetail.text = data?.kodePekerjaan
        rootView.tvJenisDetail.text = data?.idBarang
        rootView.tvSatuanDetail.text = data?.idSatuan
        rootView.tvFungsiDetail.text = data?.fungsi
        rootView.tvTargetDetail.text = data?.target
        var dataKeterangan: String? = ""

        data?.keterangan?.forEach {
             dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
        }

        rootView.tvKeteranganDetail.text = dataKeterangan
    }
}