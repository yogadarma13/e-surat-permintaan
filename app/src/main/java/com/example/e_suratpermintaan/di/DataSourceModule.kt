package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.datasource.*
import com.example.e_suratpermintaan.framework.datasourceimpl.*
import org.koin.dsl.module

val dataSourceModule = module {
    single {
        SuratPermintaanDataSourceImpl(get()) as SuratPermintaanDataSource
    }
    single {
        AuthDataSourceImpl(get()) as AuthDataSource
    }
    single {
        ProfileDataSourceImpl(get()) as ProfileDataSource
    }
    single {
        MasterDataSourceImpl(get()) as MasterDataSource
    }
    single {
        ItemSuratPermintaanSourceImpl(get()) as ItemSuratPermintaanDataSource
    }
    single {
        FileLampiranSourceImpl(get()) as FileLampiranDataSource
    }
    single {
        NotifikasiDataSourceImpl(get()) as NotifikasiDataSource
    }
}