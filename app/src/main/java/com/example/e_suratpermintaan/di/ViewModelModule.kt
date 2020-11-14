package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.AddFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.EditFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.RemoveFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.*
import com.e_suratpermintaan.core.usecases.master.*
import com.e_suratpermintaan.core.usecases.master.optionlist.GetPenugasanOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.GetStatusPenugasanOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetJenisDataFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetJenisPermintaanFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetProyekFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetStatusFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.notifikasi.GetNotifikasiListUseCase
import com.e_suratpermintaan.core.usecases.notifikasi.ReadNotifikasiUseCase
import com.e_suratpermintaan.core.usecases.profile.EditProfileUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.*
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        SharedMasterData()
    }
    viewModel {
        SuratPermintaanViewModel(
            get() as AddSuratPermintaanUseCase,
            get() as ReadAllDataSuratPermintaanUseCase,
            get() as ReadMyDataSuratPermintaanUseCase,
            get() as RemoveSuratPermintaanUseCase,
            get() as ReadDetailSuratPermintaanUseCase,
            get() as EditSuratPermintaanUseCase,
            get() as VerifikasiSuratPermintaanUseCase,
            get() as AjukanSuratPermintaanUseCase,
            get() as CancelSuratPermintaanUseCase,
            get() as ReadHistorySuratPermintaanUseCase,
            get() as SaveEditSuratPermintaanUseCase
        )
    }
    viewModel {
        AuthViewModel(
            get() as DoLoginUseCase
        )
    }
    viewModel {
        ProfileViewModel(
            get() as GetProfileUseCase,
            get() as EditProfileUseCase
        )
    }
    viewModel {
        MasterViewModel(
            get() as GetProyekListUseCase,
            get() as GetJenisListUseCase,
            get() as GetCostCodeListUseCase,
            get() as GetPersyaratanListUseCase,
            get() as GetUomListUseCase,
            get() as GetStatusFilterOptionUseCase,
            get() as GetJenisDataFilterOptionUseCase,
            get() as GetProyekFilterOptionUseCase,
            get() as GetJenisPermintaanFilterOptionUseCase,
            get() as GetPenugasanOptionUseCase,
            get() as GetStatusPenugasanOptionUseCase,
            get() as GetKategoriListUseCase
        )
    }
    viewModel {
        ItemSuratPermintaanViewModel(
            get() as AddItemSuratPermintaanUseCase,
            get() as RemoveItemSuratPermintaanUseCase,
            get() as EditItemSuratPermintaanUseCase,
            get() as ReadDetailItemSuratPermintaanUseCase,
            get() as SetPenugasanItemUseCase,
            get() as ProcessItemSuratPermintaanUseCase,
            get() as UnProcessItemSuratPermintaanUseCase
        )
    }
    viewModel {
        FileLampiranViewModel(
            get() as AddFileLampiranUseCase,
            get() as EditFileLampiranUseCase,
            get() as RemoveFileLampiranUseCase
        )
    }
    viewModel {
        NotifikasiViewModel(
            get() as GetNotifikasiListUseCase,
            get() as ReadNotifikasiUseCase
        )
    }
}