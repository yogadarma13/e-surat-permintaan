package com.example.e_suratpermintaan.framework.retrofit

import com.example.e_suratpermintaan.BuildConfig
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun provideRetrofit(): Retrofit? {
            if (retrofit == null) {
                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(Interceptor {chain ->
                        val original = chain.request()

                        val request = original.newBuilder()
                            .header("x-sm-key", BuildConfig.API_KEY)
                            .build()

                        return@Interceptor chain.proceed(request)
                    })
                    .addInterceptor(
                        BasicAuthInterceptor(
                            BuildConfig.BASIC_AUTH_USER,
                            BuildConfig.BASIC_AUTH_PASS
                        )
                    )

                val client = httpClient.build()
                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(client)
                    .build()
            }

            return retrofit
        }

        fun provideNetworkApi(retrofit: Retrofit): NetworkApi = retrofit.create(
            NetworkApi::class.java)
    }

}