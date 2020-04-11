package com.example.e_suratpermintaan.data.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {

//    companion object {
        val BASE_URL = "https://dev.karyastudio.com/e-spb/api/surat_permintaan/"
        var retrofit : Retrofit? = null

        fun getClient() : Retrofit? {
            if (retrofit == null) {
                var okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(BasicAuthInterceptor("sm4rts0ft", "?zwMAxBnS9jj"))
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            }

            return retrofit
//        }
    }
}