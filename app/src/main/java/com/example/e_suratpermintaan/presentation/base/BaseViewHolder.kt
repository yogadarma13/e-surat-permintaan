package com.example.e_suratpermintaan.presentation.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        const val ROOTVIEW = "rootview"
    }

    abstract fun bind(
        item: Any?,
        position: Int,
        listener: (item: Any?, actionString: String?) -> Unit
    )
}