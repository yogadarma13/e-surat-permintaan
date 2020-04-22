package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DataMyData
import com.e_suratpermintaan.core.domain.entities.responses.SuratPermintaan
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class SuratPermintaanAdapter(var spList: ArrayList<SuratPermintaan?>) :
    RecyclerView.Adapter<BaseViewHolder<SuratPermintaan>>() {

    // https://stackoverflow.com/questions/51087357/converting-a-generic-recyclerview-adapter-to-kotlin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SuratPermintaan> {
        val context = parent.context

        return if (spList[0] is DataMyData){
            val view = LayoutInflater.from(context).inflate(R.layout.surat_permintaan_item, parent, false)
            MyDataViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.surat_permintaan_item, parent, false)
            DataAllViewHolder(view)
        }
    }

    override fun getItemCount(): Int = spList.size

    override fun onBindViewHolder(holder: BaseViewHolder<SuratPermintaan>, position: Int) {
        val data = spList[holder.layoutPosition]
        holder.bind(data)
    }
}