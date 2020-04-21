package com.example.e_suratpermintaan.di

import com.e_suratpermintaan.core.rx.SchedulerProvider
import com.example.e_suratpermintaan.framework.providers.DefaultSchedulerProvider
import org.koin.dsl.module

val providerModule = module {
    single {
        DefaultSchedulerProvider() as SchedulerProvider
    }
}