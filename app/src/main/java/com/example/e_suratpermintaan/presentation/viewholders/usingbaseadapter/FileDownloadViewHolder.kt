package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import android.widget.Toast
import com.e_suratpermintaan.core.domain.entities.responses.FilesDetailHistory
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.file_download_item.view.*

class FileDownloadViewHolder(private val rootView: View) : BaseViewHolder(rootView)  {
    companion object {
        const val BTN_FILE = "btn_file_history"
    }
    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {
        val data = item as FilesDetailHistory

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }
        rootView.btnDownloadFile.setOnClickListener {
            listener.invoke(data, BTN_FILE)
        }
        rootView.btnDownloadFile.text = data.keterangan

    }
}