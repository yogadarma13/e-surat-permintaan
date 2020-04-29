package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.view.View
import android.widget.Toast
import com.e_suratpermintaan.core.domain.entities.responses.FilesDetailHistory
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.file_download_item.view.*

class FileDownloadViewHolder(private val rootView: View) : BaseViewHolder(rootView)  {
    override fun bind(
        item: Any?,
        position: Int,
        listener: (item: Any?, actionString: String?) -> Unit
    ) {
        val data = item as FilesDetailHistory

        rootView.setOnClickListener {
            listener(data, null)
        }

        rootView.btnDownloadFile.text = data.keterangan

        rootView.btnDownloadFile.setOnClickListener {
            Toast.makeText(rootView.context, data.dir, Toast.LENGTH_LONG).show()
        }
    }
}