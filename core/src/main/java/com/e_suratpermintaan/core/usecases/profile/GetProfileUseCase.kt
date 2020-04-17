package com.e_suratpermintaan.core.usecases.profile

import com.e_suratpermintaan.core.data.repository.ProfileRepository
import com.e_suratpermintaan.core.domain.entities.responses.ProfileResponse
import io.reactivex.rxjava3.core.Observable

class GetProfileUseCase(private val profileRepository: ProfileRepository) {
    fun invoke(id_user: String): Observable<ProfileResponse> = profileRepository.getProfile(id_user)
}