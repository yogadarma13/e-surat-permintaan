package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.repository.AuthRepository
import com.e_suratpermintaan.core.data.repository.ProfileRepository
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.rx.SchedulerProvider
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
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        ReadAllDataSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        ReadMyDataSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        RemoveSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        DoLoginUseCase(
            get() as AuthRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetProfileUseCase(
            get() as ProfileRepository,
            get() as SchedulerProvider
        )
    }
}