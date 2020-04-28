package com.example.e_suratpermintaan.framework.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.e_suratpermintaan.BuildConfig
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class NetworkClient {

    // https://square.github.io/okhttp/3.x/okhttp/okhttp3/Cache.html

    companion object {
        private const val HEADER_CACHE_CONTROL = "Cache-Control"
        private const val HEADER_PRAGMA = "Pragma"
        private var retrofit: Retrofit? = null
        private const val DISK_CACHE_SIZE = 10 * 1024 * 1024 // 10 MB

        private fun isInternetAvailable(context: Context): Boolean {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            val activeNetwork = cm!!.activeNetworkInfo
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting
        }

        fun getCache(context: Context): Cache? {
            val httpCacheDirectory = File(context.cacheDir, "responses")

            var cache: Cache? = null
            try {
                cache = Cache(httpCacheDirectory, DISK_CACHE_SIZE.toLong())
            } catch (e: IOException) {
                Log.e("MYAPP-RETROFIT", "Could not create http cache", e)
            }

            return cache
        }

        private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
//                    Log.d(
//                        "MYAPP-RETROFIT",
//                        "log: http log: $message"
//                    )
                })
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

        private fun networkInterceptor(context: Context): Interceptor {

            val credentials = Credentials.basic(
                BuildConfig.BASIC_AUTH_USER,
                BuildConfig.BASIC_AUTH_PASS
            )

            return Interceptor { chain ->
                Log.d("MYAPP-RETROFIT", "network interceptor: called.")
                val originalRequest = chain.request()

                // Authentication Header - START
                // ######################################################
                val builder = originalRequest.newBuilder()
                    .header("x-sm-key", BuildConfig.API_KEY)
                    .header("Authorization", credentials)
                // ------------------------------------------------------
                // Authentication Header - END

                val cacheControl = CacheControl.Builder()
                    .noCache()
                    .maxAge(5, TimeUnit.SECONDS)
                    //.maxAge(0, TimeUnit.SECONDS)
                    .build()

                val authenticatedRequest = builder
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()

                val response = chain.proceed(authenticatedRequest)
                response
            }
        }

        private fun offlineInterceptor(context: Context): Interceptor {
            return Interceptor { chain ->
                Log.d("MYAPP-RETROFIT", "offline interceptor: called.")
                var request: Request = chain.request()

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!isInternetAvailable(context)) {
                    Log.d("MYAPP-RETROFIT", "offline interceptor: called. (IS ONLINE FALSE)")
                    val cacheControl = CacheControl.Builder()
                        //.onlyIfCached()
                        //.maxStale(7, TimeUnit.DAYS)
                        .build()

                    request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()

                    chain.proceed(request)
                } else {
                    Log.d("MYAPP-RETROFIT", "offline interceptor: called. (IS ONLINE TRUE)")
                    chain.proceed(request)
                }
            }
        }


        fun provideRetrofit(context: Context): Retrofit? {
            if (retrofit == null) {
                val httpClientBuilder = OkHttpClient.Builder()
                    .cache(getCache(context))
                    .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                    //.addInterceptor(offlineInterceptor(context))
                    .addNetworkInterceptor(networkInterceptor(context)) // only used when network is on

                httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)

                val client = httpClientBuilder.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun provideNetworkApi(retrofit: Retrofit): NetworkApi = retrofit.create(
            NetworkApi::class.java
        )
    }

}