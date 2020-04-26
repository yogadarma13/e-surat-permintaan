package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.AddFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.EditFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.filelampiran.RemoveFileLampiranUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.AddItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.EditItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.ReadDetailItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.RemoveItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.master.*
import com.e_suratpermintaan.core.usecases.notifikasi.GetNotifikasiListUseCase
import com.e_suratpermintaan.core.usecases.notifikasi.ReadNotifikasiUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.*
import com.example.e_suratpermintaan.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        SharedViewModel()
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
            get() as ReadHistorySuratPermintaanUseCase
        )
    }
    viewModel {
        AuthViewModel(
            get() as DoLoginUseCase
        )
    }
    viewModel {
        ProfileViewModel(
            get() as GetProfileUseCase
        )
    }
    viewModel {
        MasterViewModel(
            get() as GetProyekListUseCase,
            get() as GetJenisListUseCase,
            get() as GetCostCodeListUseCase,
            get() as GetPersyaratanListUseCase,
            get() as GetUomListUseCase
        )
    }
    viewModel {
        ItemSuratPermintaanViewModel(
            get() as AddItemSuratPermintaanUseCase,
            get() as RemoveItemSuratPermintaanUseCase,
            get() as EditItemSuratPermintaanUseCase,
            get() as ReadDetailItemSuratPermintaanUseCase
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