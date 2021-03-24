package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable

class MasterRepository(private val masterDataSource: MasterDataSource) :
    MasterDataSource {

//    override fun getProyekList(idUser: String): Observable<MasterProyekResponse> =
//        masterDataSource.getProyekList(idUser)

    override fun getJenisList(idUser: String): Observable<MasterJenisResponse> =
        masterDataSource.getJenisList(idUser)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        masterDataSource.getCostCodeList(id)

    override fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse> =
        masterDataSource.getPersyaratanList(id)

    override fun getUomList(id: String): Observable<MasterUOMResponse> =
        masterDataSource.getUomList(id)

    override fun getStatusFilterOptionList(idUser: String): Observable<MasterStatusFilterOptionResponse> =
        masterDataSource.getStatusFilterOptionList(idUser)

//    override fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse> =
//        masterDataSource.getJenisDataFilterOptionList()

    override fun getProyekFilterOptionList(idUser: String): Observable<MasterProyekFilterOptionResponse> =
        masterDataSource.getProyekFilterOptionList(idUser)

    override fun getJenisPermintaanFilterOptionList(idUser: String): Observable<MasterJenisPermintaanFilterOptionResponse> =
        masterDataSource.getJenisPermintaanFilterOptionList(idUser)

    override fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse> =
        masterDataSource.getPenugasanOptionList()

    override fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse> =
        masterDataSource.getStatusPenugasanOptionList()

    override fun getKodePekerjaanList(id: String): Observable<MasterKodePekerjaanResponse> =
        masterDataSource.getKodePekerjaanList(id)

    override fun getItemCodeLlist(id: String, keyword: String): Observable<MasterItemCodeResponse> =
        masterDataSource.getItemCodeLlist(id, keyword)

}