package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class EmptyViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    override fun bind(
        item: Any?,
        position: Int,
        listener: (item: Any?, actionString: String?) -> Unit
    ) {

    }

}