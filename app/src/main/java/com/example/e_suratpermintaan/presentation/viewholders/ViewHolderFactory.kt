package com.example.e_suratpermintaan.presentation.viewholders

import androidx.viewbinding.ViewBinding
import com.example.e_suratpermintaan.databinding.*
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.*
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.*

class ViewHolderFactory {
    companion object {
        fun <T : BaseViewHolder> create(view: ViewBinding, clazz: Class<T>): BaseViewHolder {
            return when (clazz) {
                CCViewHolder::class.java -> CCViewHolder(
                    view as ItemSimpleRowBinding
                )
                UomViewHolder::class.java -> UomViewHolder(
                    view as ItemSimpleRowBinding
                )
                KategoriViewHolder::class.java -> KategoriViewHolder(
                    view as ItemSimpleRowBinding
                )
                JenisBarangViewHolder::class.java -> JenisBarangViewHolder(
                    view as ItemSimpleRowBinding
                )
                MyDataViewHolder::class.java -> MyDataViewHolder(
                    view as ItemSuratPermintaanRowBinding
                )
                DataAllViewHolder::class.java -> DataAllViewHolder(
                    view.root
                )
                ItemSuratPermintaanViewHolder::class.java -> ItemSuratPermintaanViewHolder(
                    view as ItemSuratPermintaanItemRowBinding
                )
                FileDownloadViewHolder::class.java -> FileDownloadViewHolder(
                    view as FileDownloadItemBinding
                )
                PersyaratanViewHolder::class.java -> PersyaratanViewHolder(
                    view as ItemSimpleCheckboxBinding
                )
                PenugasanViewHolder::class.java -> PenugasanViewHolder(
                    view as ItemSimpleRowBinding
                )
                StatusPenugasanViewHolder::class.java -> StatusPenugasanViewHolder(
                    view as ItemSimpleRowBinding
                )
                EditItemSuratPermintaanViewHolder::class.java -> EditItemSuratPermintaanViewHolder(
                    view as ItemSuratPermintaanItemRowBinding
                )
                EditFileSuratPermintaanViewHolder::class.java -> EditFileSuratPermintaanViewHolder(
                    view as ItemFileLampiranRowBinding
                )
                FileSuratPermintaanViewHolder::class.java -> FileSuratPermintaanViewHolder(
                    view as ItemFileLampiranRowBinding
                )
                NotifikasiViewHolder::class.java -> NotifikasiViewHolder(
                    view as NotifikasiUnreadListBinding
                )
                else -> EmptyViewHolder(view.root)
            }
        }
    }
}