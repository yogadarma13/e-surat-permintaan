package com.e_suratpermintaan.core.usecases.profile

import com.e_suratpermintaan.core.data.repository.ProfileRepository
import com.e_suratpermintaan.core.domain.entities.responses.EditProfileResponse
import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val schedulerProvider: SchedulerProvider
) {
    fun invoke(id: RequestBody,
               email: RequestBody,
               passwordLast: RequestBody,
               passwordNew: RequestBody,
               name: RequestBody,
               desc: RequestBody,
               file: MultipartBody.Part?,
               ttd: MultipartBody.Part?): Observable<EditProfileResponse> =
        profileRepository.editProfile(id, email, passwordLast, passwordNew, name, desc, file, ttd)
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.mainThread)
}