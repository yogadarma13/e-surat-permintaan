package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.datasource.*
import com.e_suratpermintaan.core.data.repository.*
import org.koin.dsl.module

val repoModule = module {
    single {
        SuratPermintaanRepository(
            get() as SuratPermintaanDataSource
        )
    }
    single {
        AuthRepository(
            get() as AuthDataSource
        )
    }
    single {
        ProfileRepository(
            get() as ProfileDataSource
        )
    }
    single {
        MasterRepository(
            get() as MasterDataSource
        )
    }
    single {
        ItemSuratPermintaanRepository(
            get() as ItemSuratPermintaanDataSource
        )
    }
    single {
        FileLampiranRepository(
            get() as FileLampiranDataSource
        )
    }
    single {
        NotifikasiRepository(
            get() as NotifikasiDataSource
        )
    }
}