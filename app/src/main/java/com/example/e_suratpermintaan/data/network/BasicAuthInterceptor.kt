package com.example.e_suratpermintaan.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(user: String, password: String) : Interceptor {

    private var credentials = Credentials.basic(user, password)


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}