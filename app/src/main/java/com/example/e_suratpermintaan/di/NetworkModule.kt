package com.example.e_suratpermintaan.di

import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import com.example.e_suratpermintaan.framework.retrofit.NetworkClient
import okhttp3.Cache
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        NetworkClient.provideRetrofit(get()) as Retrofit
    }
    single {
        NetworkClient.provideNetworkApi(get()) as NetworkApi
    }
    single {
        NetworkClient.getCache((get())) as Cache
    }
}