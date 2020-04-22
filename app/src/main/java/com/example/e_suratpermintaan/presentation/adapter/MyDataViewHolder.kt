package com.example.e_suratpermintaan.presentation.adapter

import android.view.View
import com.e_suratpermintaan.core.domain.entities.responses.DataMyData
import com.e_suratpermintaan.core.domain.entities.responses.SuratPermintaan
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class MyDataViewHolder(rootView: View) : BaseViewHolder<SuratPermintaan>(rootView) {

    init {

    }

    override fun bind(item: SuratPermintaan) {
        val data = item as DataMyData
    }

}