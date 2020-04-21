package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.master.GetCostCodeListUseCase
import com.e_suratpermintaan.core.usecases.master.GetJenisListUseCase
import com.e_suratpermintaan.core.usecases.master.GetProyekListUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.AddSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.ReadAllDataSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.ReadMyDataSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.RemoveSuratPermintaanUseCase
import com.example.e_suratpermintaan.presentation.viewmodel.AuthViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SuratPermintaanViewModel(
            get() as AddSuratPermintaanUseCase,
            get() as ReadAllDataSuratPermintaanUseCase,
            get() as ReadMyDataSuratPermintaanUseCase,
            get() as RemoveSuratPermintaanUseCase
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
            get() as GetCostCodeListUseCase
        )
    }
}