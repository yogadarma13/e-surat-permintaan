package com.example.e_suratpermintaan.framework.retrofit

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(user: String, password: String) : Interceptor {

    private var credentials = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}