package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.AddSuratPermintaanUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadAllDataSuratPermintaanUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadMyDataSuratPermintaanUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.RemoveSuratPermintaanUseCase
import com.example.e_suratpermintaan.presentation.viewmodels.AuthViewModel
import com.example.e_suratpermintaan.presentation.viewmodels.SuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodels.ProfileViewModel
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
        AuthViewModel(
            get() as DoLoginUseCase
        )
        ProfileViewModel(
            get() as GetProfileUseCase
        )
    }
}