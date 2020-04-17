package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.AuthDataSource
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class AuthDataSourceImpl(private val networkApi: NetworkApi) :
    AuthDataSource {

    override fun doLogin(login: Login): Observable<LoginResponse> = networkApi.loginUser(login)

}