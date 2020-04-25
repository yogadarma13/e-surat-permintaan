package com.example.e_suratpermintaan.di

import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import org.koin.dsl.module

val preferenceModule = module {
    single {
        ProfilePreference(get())
    }
    single {
        FCMPreference(get())
    }
}