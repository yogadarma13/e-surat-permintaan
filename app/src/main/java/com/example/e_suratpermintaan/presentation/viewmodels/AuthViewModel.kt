package com.example.e_suratpermintaan.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.AuthDataSource
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import com.e_suratpermintaan.core.usecases.auth.DoLoginUseCase
import io.reactivex.rxjava3.core.Observable

class AuthViewModel(
    private val doLoginUseCase: DoLoginUseCase
) : ViewModel(), AuthDataSource {

    override fun doLogin(login: Login): Observable<LoginResponse> = doLoginUseCase.invoke(login)

}