package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.AuthDataSource
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import io.reactivex.rxjava3.core.Observable

class AuthRepository(private val authDataSource: AuthDataSource) :
    AuthDataSource {

    override fun doLogin(login: Login): Observable<LoginResponse> = authDataSource.doLogin(login)

}