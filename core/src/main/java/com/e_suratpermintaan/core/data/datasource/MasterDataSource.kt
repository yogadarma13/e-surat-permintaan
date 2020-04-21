package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisProyekResponse
import io.reactivex.rxjava3.core.Observable

interface MasterDataSource {

    fun getProyekList(id_user: String): Observable<MasterJenisProyekResponse>

    fun getJenisList(id_user: String): Observable<MasterJenisProyekResponse>

    fun getCostCodeList(id: String): Observable<MasterCCResponse>
}