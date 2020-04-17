package com.example.e_suratpermintaan.di

import com.example.e_suratpermintaan.framework.retrofit.NetworkClient
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        NetworkClient.provideRetrofit() as Retrofit
    }
    single {
        NetworkClient.provideNetworkApi(get())
    }
}