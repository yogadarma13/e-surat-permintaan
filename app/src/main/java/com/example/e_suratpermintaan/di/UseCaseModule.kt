package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.repository.*
import com.e_suratpermintaan.core.rx.SchedulerProvider
import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.AddItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.EditItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.ReadDetailItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.itemsuratpermintaan.RemoveItemSuratPermintaanUseCase
import com.e_suratpermintaan.core.usecases.master.GetCostCodeListUseCase
import com.e_suratpermintaan.core.usecases.master.GetJenisListUseCase
import com.e_suratpermintaan.core.usecases.master.GetPersyaratanListUseCase
import com.e_suratpermintaan.core.usecases.master.GetProyekListUseCase
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import com.e_suratpermintaan.core.usecases.suratpermintaan.*
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
        ReadDetailSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        EditSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        VerifikasiSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        AjukanSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        CancelSuratPermintaanUseCase(
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
    single {
        GetPersyaratanListUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        AddItemSuratPermintaanUseCase(
            get() as ItemSuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        RemoveItemSuratPermintaanUseCase(
            get() as ItemSuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        EditItemSuratPermintaanUseCase(
            get() as ItemSuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        ReadDetailItemSuratPermintaanUseCase(
            get() as ItemSuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
}