package com.e_suratpermintaan.core.usecases.auth

import com.e_suratpermintaan.core.data.repository.AuthRepository
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.LoginResponse
import io.reactivex.rxjava3.core.Observable

class DoLoginUseCase(private val authRepository: AuthRepository){
    fun invoke(login: Login): Observable<LoginResponse> = authRepository.doLogin(login)
}