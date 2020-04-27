package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class ViewHolderFactory {
    companion object {
        fun <T : BaseViewHolder> create(view: View, clazz: Class<T>): BaseViewHolder {
            return when (clazz) {
                CCViewHolder::class.java -> CCViewHolder(view)
                UomViewHolder::class.java -> UomViewHolder(view)
                JenisBarangViewHolder::class.java -> JenisBarangViewHolder(view)
                MyDataViewHolder::class.java -> MyDataViewHolder(view)
                DataAllViewHolder::class.java -> DataAllViewHolder(view)
                ItemSuratPermintaanViewHolder::class.java -> ItemSuratPermintaanViewHolder(view)
                DetailHistoryViewHolder::class.java -> DetailHistoryViewHolder(view)
                FileDownloadViewHolder::class.java -> FileDownloadViewHolder(view)
                else -> EmptyViewHolder(view)
            }
        }
    }
}