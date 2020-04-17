package com.e_suratpermintaan.domain.usecases.suratpermintaan

import com.e_suratpermintaan.core.domain.entities.responses.DeleteSPResponse
import com.e_suratpermintaan.core.data.repository.SuratPermintaanRepository
import io.reactivex.rxjava3.core.Observable

class RemoveSuratPermintaan(private val suratPermintaanRepository: SuratPermintaanRepository) {
    fun invoke(id_sp: String): Observable<DeleteSPResponse> = suratPermintaanRepository.remove(id_sp)
}