package com.example.e_suratpermintaan

import android.app.Application
import com.e_suratpermintaan.core.data.datasource.SuratPermintaanDataSource
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import com.e_suratpermintaan.domain.usecases.suratpermintaan.AddSuratPermintaan
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadAllDataSuratPermintaan
import com.e_suratpermintaan.domain.usecases.suratpermintaan.ReadMyDataSuratPermintaan
import com.e_suratpermintaan.domain.usecases.suratpermintaan.RemoveSuratPermintaan
import com.example.e_suratpermintaan.framework.datasourceimpl.SuratPermintaanDataSourceImpl
import com.example.e_suratpermintaan.framework.retrofit.NetworkClient
import com.example.e_suratpermintaan.presentation.main.SuratPermintaanViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(networkModule, dataSourceModule, repoModule, useCaseModule, viewModelModule)
        }
    }

    companion object {
        private val networkModule = module {
            single {
                NetworkClient.provideRetrofit() as Retrofit
            }
            single {
                NetworkClient.provideNetworkApi(get())
            }
        }

        private val dataSourceModule: Module = module {
            single {
                SuratPermintaanDataSourceImpl(get()) as SuratPermintaanDataSource
            }
        }

        private val repoModule = module {
            factory {
                SuratPermintaanRepository(
                    get() as SuratPermintaanDataSource
                )
            }
        }

        private val viewModelModule = module {
            viewModel {
                SuratPermintaanViewModel(
                    get() as AddSuratPermintaan,
                    get() as ReadAllDataSuratPermintaan,
                    get() as ReadMyDataSuratPermintaan,
                    get() as RemoveSuratPermintaan
                )
            }
        }

        private val useCaseModule = module {
            factory {
                AddSuratPermintaan(
                    get() as SuratPermintaanRepository
                )
            }
            factory {
                ReadAllDataSuratPermintaan(
                    get() as SuratPermintaanRepository
                )
            }
            factory {
                ReadMyDataSuratPermintaan(
                    get() as SuratPermintaanRepository
                )
            }
            factory {
                RemoveSuratPermintaan(
                    get() as SuratPermintaanRepository
                )
            }
        }
    }
}