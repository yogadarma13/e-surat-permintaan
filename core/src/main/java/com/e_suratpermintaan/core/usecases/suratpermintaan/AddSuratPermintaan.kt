package com.e_suratpermintaan.domain.usecases.suratpermintaan

import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.CreateSPResponse
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import io.reactivex.rxjava3.core.Observable

class AddSuratPermintaan(private val suratPermintaanRepository: SuratPermintaanRepository){
    fun invoke(sp: CreateSP): Observable<CreateSPResponse> = suratPermintaanRepository.add(sp);
}