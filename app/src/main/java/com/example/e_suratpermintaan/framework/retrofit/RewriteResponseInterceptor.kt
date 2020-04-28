package com.example.e_suratpermintaan.framework.retrofit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

abstract class RewriteResponseInterceptor : Interceptor {

    abstract fun isInternetAvailable(): Boolean

    abstract fun onInternetUnavailable()

    abstract fun onCacheUnavailable()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val cacheControl = originalResponse.header("Cache-Control")

        if (!isInternetAvailable()) {
            return originalResponse
        }

        return if (cacheControl == null
                || cacheControl.contains("no-store")
                || cacheControl.contains("no-cache")
                || cacheControl.contains("must-revalidate")
                || cacheControl.contains("max-age=0")
            ) {

                val request = chain.request()
                val builder = request.newBuilder()

                /*
                *  If there is Internet, get the cache that was stored 5 seconds ago.
                *  If the cache is older than 5 seconds, then discard it,
                *  and indicate an error in fetching the response.
                *  The 'max-age' attribute is responsible for this behavior.
                */

                val cacheControl =
                    CacheControl.Builder().maxAge(5, TimeUnit.SECONDS).build()

                val authenticatedRequest = builder
                    //.header("Cache-Control", cacheControl.toString())
                    .header("Cache-Control", "public, max-age=" + 5)
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .build()

                val modifiedResponse = chain.proceed(authenticatedRequest)
                modifiedResponse
            } else {
                originalResponse
            }
    }
}