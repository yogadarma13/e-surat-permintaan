package com.example.e_suratpermintaan.framework.retrofit

import com.example.e_suratpermintaan.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

abstract class BasicAuthInterceptor(user: String, password: String) : Interceptor {

    abstract fun isInternetAvailable(): Boolean

    abstract fun onInternetUnavailable()

    abstract fun onCacheUnavailable()

    private var credentials = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (!isInternetAvailable()) {
            return chain.proceed(originalRequest)
        }

        val builder = originalRequest.newBuilder()
            .header("x-sm-key", BuildConfig.API_KEY)
            .header("Authorization", credentials)
        val authenticatedRequest = builder.build()

        return chain.proceed(authenticatedRequest)
    }
}