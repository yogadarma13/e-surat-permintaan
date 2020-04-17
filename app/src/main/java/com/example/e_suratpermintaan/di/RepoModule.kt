package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.data.datasource.AuthDataSource
import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.data.repository.AuthRepository
import com.e_suratpermintaan.core.data.repository.ProfileRepository
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        SuratPermintaanRepository(
            get() as SuratPermintaanDataSource
        )
        AuthRepository(
            get() as AuthDataSource
        )
        ProfileRepository(
            get() as ProfileDataSource
        )
    }
}