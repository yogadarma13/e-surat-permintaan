package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class ItemSuratPermintaanAdapter(var spList: ArrayList<ItemsDetailSP?>) :
    RecyclerView.Adapter<BaseViewHolder<ItemsDetailSP>>() {

    private lateinit var onClickItemListener: OnClickItemListener

    interface OnClickItemListener {
        fun onClick(view: View, item: ItemsDetailSP?)
    }

    fun setOnClickListener(onClickItemListener: OnClickItemListener) {
        this.onClickItemListener = onClickItemListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemsDetailSP> {
        val context = parent.context
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_surat_permintaan_row, parent, false)
        return ItemSuratPermintaanViewHolder(view)
    }

    override fun getItemCount(): Int = spList.size

    override fun onBindViewHolder(holder: BaseViewHolder<ItemsDetailSP>, position: Int) {
        val data = spList[holder.layoutPosition]
        holder.itemView.setOnClickListener {
            onClickItemListener.onClick(holder.itemView, data)
        }
        holder.bind(data)
    }
}