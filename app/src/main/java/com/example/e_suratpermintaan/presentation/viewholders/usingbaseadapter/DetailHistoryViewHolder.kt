package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_detail_history.view.*

class DetailHistoryViewHolder(private val rootView: View) :
    BaseViewHolder(rootView) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (item: Any?, actionString: String?) -> Unit
    ) {
        val data = item as ItemsDetailHistory

        rootView.setOnClickListener {
            listener(data, null)
        }

        rootView.tvKodeDetail.text = data.kodePekerjaan
        rootView.tvJenisDetail.text = data.idBarang
        rootView.tvSatuanDetail.text = data.idSatuan
        rootView.tvFungsiDetail.text = data.fungsi
        rootView.tvTargetDetail.text = data.target
        rootView.tvQty.text = data.qty

        var dataKeterangan: String? = ""

        data.keterangan?.forEach {
            dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
        }

        rootView.tvKeteranganDetail.text = dataKeterangan
    }
}