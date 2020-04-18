package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.example.e_suratpermintaan.framework.datasourceimpl.AuthDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.ProfileDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.SuratPermintaanDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single {
        SuratPermintaanDataSourceImpl(get()) as SuratPermintaanDataSource
    }
    single {
        AuthDataSourceImpl(get())
    }
    single {
        ProfileDataSourceImpl(get())
    }
}