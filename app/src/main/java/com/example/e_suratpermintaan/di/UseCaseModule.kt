package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.repository.AuthRepository
import com.e_suratpermintaan.core.data.repository.ProfileRepository
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.AddSuratPermintaanUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadAllDataSuratPermintaanUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadMyDataSuratPermintaanUseCase
import com.e_suratpermintaan.domain.usecases.suratpermintaan.RemoveSuratPermintaanUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single {
        AddSuratPermintaanUseCase(
            get() as SuratPermintaanRepository
        )
    }
    single {
        ReadAllDataSuratPermintaanUseCase(
            get() as SuratPermintaanRepository
        )
    }
    single {
        ReadMyDataSuratPermintaanUseCase(
            get() as SuratPermintaanRepository
        )
    }
    single {
        RemoveSuratPermintaanUseCase(
            get() as SuratPermintaanRepository
        )
    }
    single {
        DoLoginUseCase(
            get() as AuthRepository
        )
    }
    single {
        GetProfileUseCase(
            get() as ProfileRepository
        )
    }
}