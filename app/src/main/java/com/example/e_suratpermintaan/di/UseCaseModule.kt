package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.repository.AuthRepository
import com.e_suratpermintaan.core.data.repository.MasterRepository
import com.e_suratpermintaan.core.data.repository.ProfileRepository
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.core.rx.SchedulerProvider
import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.master.GetCostCodeListUseCase
import com.e_suratpermintaan.core.usecases.master.GetJenisListUseCase
import com.e_suratpermintaan.core.usecases.master.GetProyekListUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.AddSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.ReadAllDataSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.ReadMyDataSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.RemoveSuratPermintaanUseCase
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
    single {
        GetProyekListUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetJenisListUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetCostCodeListUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
}