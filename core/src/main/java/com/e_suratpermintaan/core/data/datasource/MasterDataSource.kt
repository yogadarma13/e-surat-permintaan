package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterPersyaratanResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterProyekResponse
import io.reactivex.rxjava3.core.Observable

interface MasterDataSource {

    fun getProyekList(id_user: String): Observable<MasterProyekResponse>

    fun getJenisList(id_user: String): Observable<MasterJenisResponse>

    fun getCostCodeList(id: String): Observable<MasterCCResponse>

    fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse>
}