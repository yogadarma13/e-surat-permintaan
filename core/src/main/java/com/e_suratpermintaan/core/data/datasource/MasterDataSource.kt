package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable

interface MasterDataSource {

    fun getProyekList(id_user: String): Observable<MasterProyekResponse>

    fun getJenisList(id_user: String): Observable<MasterJenisResponse>

    fun getCostCodeList(id: String): Observable<MasterCCResponse>

    fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse>

    fun getUomList(id: String): Observable<MasterUOMResponse>

    fun getStatusFilterOptionList(id_user: String): Observable<MasterStatusFilterOptionResponse>

    fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse>

    fun getProyekFilterOptionList(id_user: String): Observable<MasterProyekFilterOptionResponse>

    fun getJenisPermintaanFilterOptionList(id_user: String): Observable<MasterJenisPermintaanFilterOptionResponse>

    fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse>

    fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse>
}