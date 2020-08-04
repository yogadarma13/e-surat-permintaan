package com.example.e_suratpermintaan.presentation.viewholders

import android.view.View
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.*
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.*

class ViewHolderFactory {
    companion object {
        fun <T : BaseViewHolder> create(view: View, clazz: Class<T>): BaseViewHolder {
            return when (clazz) {
                CCViewHolder::class.java -> CCViewHolder(
                    view
                )
                UomViewHolder::class.java -> UomViewHolder(
                    view
                )
                KategoriViewHolder::class.java -> KategoriViewHolder(
                    view
                )
                JenisBarangViewHolder::class.java -> JenisBarangViewHolder(
                    view
                )
                MyDataViewHolder::class.java -> MyDataViewHolder(
                    view
                )
                DataAllViewHolder::class.java -> DataAllViewHolder(
                    view
                )
                ItemSuratPermintaanViewHolder::class.java -> ItemSuratPermintaanViewHolder(
                    view
                )
                FileDownloadViewHolder::class.java -> FileDownloadViewHolder(
                    view
                )
                PersyaratanViewHolder::class.java -> PersyaratanViewHolder(
                    view
                )
                PenugasanViewHolder::class.java -> PenugasanViewHolder(
                    view
                )
                StatusPenugasanViewHolder::class.java -> StatusPenugasanViewHolder(
                    view
                )
                EditItemSuratPermintaanViewHolder::class.java -> EditItemSuratPermintaanViewHolder(
                    view
                )
                EditFileSuratPermintaanViewHolder::class.java -> EditFileSuratPermintaanViewHolder(
                    view
                )
                FileSuratPermintaanViewHolder::class.java -> FileSuratPermintaanViewHolder(
                    view
                )
                NotifikasiViewHolder::class.java -> NotifikasiViewHolder(
                    view
                )
                else -> EmptyViewHolder(view)
            }
        }
    }
}