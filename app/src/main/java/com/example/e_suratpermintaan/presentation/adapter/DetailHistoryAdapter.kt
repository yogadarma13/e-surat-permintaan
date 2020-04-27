package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class DetailHistoryAdapter(var dataItemHistory: List<ItemsDetailHistory>):
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.detail_history_item, parent, false)

        return DetailHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataItemHistory?.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = dataItemHistory[holder.layoutPosition]

        holder.bind(data)
    }
}