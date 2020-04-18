package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.ProfileDataSource
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import com.e_suratpermintaan.core.usecases.profile.GetProfileUseCase
import io.reactivex.rxjava3.core.Observable

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel(), ProfileDataSource {

    override fun getProfile(id_user: String): Observable<ProfileResponse> =
        getProfileUseCase.invoke(id_user)

}