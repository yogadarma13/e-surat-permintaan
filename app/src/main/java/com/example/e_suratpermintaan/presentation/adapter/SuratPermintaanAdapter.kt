package com.example.e_suratpermintaan.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.DataMyData
import com.e_suratpermintaan.core.domain.entities.responses.SuratPermintaan
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class SuratPermintaanAdapter(private val layoutRes: Int, var spList: ArrayList<SuratPermintaan>) :
    RecyclerView.Adapter<BaseViewHolder<SuratPermintaan>>() {

    // https://stackoverflow.com/questions/51087357/converting-a-generic-recyclerview-adapter-to-kotlin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SuratPermintaan> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(layoutRes, parent)

        return if (spList.toArray().isArrayOf<DataMyData>()){
            MyDataViewHolder(view)
        } else {
            DataAllViewHolder(view)
        }
    }

    override fun getItemCount(): Int = spList.size

    override fun onBindViewHolder(holder: BaseViewHolder<SuratPermintaan>, position: Int) {
        val data = spList[holder.layoutPosition]
        holder.bind(data)
    }
}