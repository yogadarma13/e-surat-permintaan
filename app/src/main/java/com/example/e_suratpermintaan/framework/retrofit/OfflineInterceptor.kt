package com.example.e_suratpermintaan.framework.retrofit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

abstract class OfflineInterceptor : Interceptor {

    abstract fun isInternetAvailable(): Boolean

    abstract fun onInternetUnavailable()

    abstract fun onCacheUnavailable()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var builder = originalRequest.newBuilder()

        /*
        *  Leveraging the advantage of using Kotlin,
        *  we initialize the request and change its header depending on whether
        *  the device is connected to Internet or not.
        */

        if (!isInternetAvailable()) {
            /*
            *  If there is no Internet, get the cache that was stored 7 days ago.
            *  If the cache is older than 7 days, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-stale' attribute is responsible for this behavior.
            *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
            */
            val cacheControl =
                CacheControl.Builder().onlyIfCached().maxStale(7, TimeUnit.DAYS).build()

            builder = builder
//                .cacheControl(cacheControl)
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                )
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")

            onInternetUnavailable()

            val authenticatedRequest = builder.build()
            val response = chain.proceed(authenticatedRequest)
            if (response.cacheResponse() == null) {
                onCacheUnavailable()
            }
            return response
        }

        return chain.proceed(originalRequest)
    }
}