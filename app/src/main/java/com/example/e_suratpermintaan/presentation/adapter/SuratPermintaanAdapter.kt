package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DataMyData
import com.e_suratpermintaan.core.domain.entities.responses.SuratPermintaan
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class SuratPermintaanAdapter() :
    RecyclerView.Adapter<BaseViewHolder<SuratPermintaan>>() {

    var spList: ArrayList<SuratPermintaan?> = arrayListOf()
    private lateinit var onClickItemListener: OnClickItemListener

    // https://stackoverflow.com/questions/51087357/converting-a-generic-recyclerview-adapter-to-kotlin

    interface OnClickItemListener {
        fun onClick(view: View, item: SuratPermintaan?)
    }

    fun setOnClickListener(onClickItemListener: OnClickItemListener){
        this.onClickItemListener = onClickItemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SuratPermintaan> {
        val context = parent.context

        return if (spList[0] is DataMyData){
            val view = LayoutInflater.from(context).inflate(R.layout.surat_permintaan_row, parent, false)
            MyDataViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.surat_permintaan_row, parent, false)
            DataAllViewHolder(view)
        }
    }

    override fun getItemCount(): Int = spList.size

    override fun onBindViewHolder(holder: BaseViewHolder<SuratPermintaan>, position: Int) {
        val data = spList[holder.layoutPosition]
        holder.itemView.setOnClickListener {
            onClickItemListener.onClick(holder.itemView, data)
        }
        holder.bind(data)
    }
}