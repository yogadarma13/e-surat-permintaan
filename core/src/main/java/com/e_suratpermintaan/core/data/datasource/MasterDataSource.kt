package com.e_suratpermintaan.core.data.datasource

import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable

interface MasterDataSource {

//    fun getProyekList(idUser: String): Observable<MasterProyekResponse>

    fun getJenisList(idUser: String): Observable<MasterJenisResponse>

    fun getCostCodeList(id: String): Observable<MasterCCResponse>

    fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse>

    fun getUomList(id: String): Observable<MasterUOMResponse>

    fun getStatusFilterOptionList(idUser: String): Observable<MasterStatusFilterOptionResponse>

//    fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse>

    fun getProyekFilterOptionList(idUser: String): Observable<MasterProyekFilterOptionResponse>

    fun getJenisPermintaanFilterOptionList(idUser: String): Observable<MasterJenisPermintaanFilterOptionResponse>

    fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse>

    fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse>

    fun getKodePekerjaanList(id: String): Observable<MasterKodePekerjaanResponse>

    fun getItemCodeLlist(id: String, keyword: String): Observable<MasterItemCodeResponse>
}