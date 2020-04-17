package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import io.reactivex.rxjava3.core.Observable

interface AuthDataSource {

    fun doLogin(login: Login): Observable<LoginResponse>

}