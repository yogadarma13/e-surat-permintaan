package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.datasource.AuthDataSource
import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.example.e_suratpermintaan.framework.datasourceimpl.AuthDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.MasterDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.ProfileDataSourceImpl
import com.example.e_suratpermintaan.framework.datasourceimpl.SuratPermintaanDataSourceImpl
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
}