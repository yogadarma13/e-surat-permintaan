package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.repository.*
import com.e_suratpermintaan.core.rx.SchedulerProvider
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
import org.koin.dsl.module

val useCaseModule = module {

    // ===========================================SURAT PERMINTAAN =======================================
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
        ReadHistorySuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }
    single {
        SaveEditSuratPermintaanUseCase(
            get() as SuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }

    // ================================================ LOGIN & PROFILE ======================================
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
        EditProfileUseCase(
            get() as ProfileRepository,
            get() as SchedulerProvider
        )
    }

    // ========================================= MASTER ===========================================
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
        GetUomListUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetStatusFilterOptionUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetJenisDataFilterOptionUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetProyekFilterOptionUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetJenisPermintaanFilterOptionUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetPenugasanOptionUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }
    single {
        GetStatusPenugasanOptionUseCase(
            get() as MasterRepository,
            get() as SchedulerProvider
        )
    }

    // ===================================== SURAT PERMINTAAN (ITEM) ====================================
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
    single {
        SetPenugasanItemUseCase(
            get() as ItemSuratPermintaanRepository,
            get() as SchedulerProvider
        )
    }

    // =================================== FILE LAMPIRAN ===========================================
    single {
        AddFileLampiranUseCase(
            get() as FileLampiranRepository,
            get() as SchedulerProvider
        )
    }
    single {
        EditFileLampiranUseCase(
            get() as FileLampiranRepository,
            get() as SchedulerProvider
        )
    }
    single {
        RemoveFileLampiranUseCase(
            get() as FileLampiranRepository,
            get() as SchedulerProvider
        )
    }

    // =============================== NOTIFIKASI ===============================================
    single {
        GetNotifikasiListUseCase(
            get() as NotifikasiRepository,
            get() as SchedulerProvider
        )
    }
    single {
        ReadNotifikasiUseCase(
            get() as NotifikasiRepository,
            get() as SchedulerProvider
        )
    }
}