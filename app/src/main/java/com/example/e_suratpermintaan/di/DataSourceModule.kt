package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.example.e_suratpermintaan.framework.datasourceimpl.AuthDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.ProfileDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.SuratPermintaanDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val dataSourceModule: Module = module {
    single {
        SuratPermintaanDataSourceImpl(get()) as SuratPermintaanDataSource
        AuthDataSourceImpl(get())
        ProfileDataSourceImpl(get())
    }
}