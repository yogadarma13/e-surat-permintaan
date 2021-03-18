package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable

class MasterRepository(private val masterDataSource: MasterDataSource) :
    MasterDataSource {

    override fun getProyekList(id_user: String): Observable<MasterProyekResponse> =
        masterDataSource.getProyekList(id_user)

    override fun getJenisList(id_user: String): Observable<MasterJenisResponse> =
        masterDataSource.getJenisList(id_user)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        masterDataSource.getCostCodeList(id)

    override fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse> =
        masterDataSource.getPersyaratanList(id)

    override fun getUomList(id: String): Observable<MasterUOMResponse> =
        masterDataSource.getUomList(id)

    override fun getStatusFilterOptionList(id_user: String): Observable<MasterStatusFilterOptionResponse> =
        masterDataSource.getStatusFilterOptionList(id_user)

    override fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse> =
        masterDataSource.getJenisDataFilterOptionList()

    override fun getProyekFilterOptionList(id_user: String): Observable<MasterProyekFilterOptionResponse> =
        masterDataSource.getProyekFilterOptionList(id_user)

    override fun getJenisPermintaanFilterOptionList(id_user: String): Observable<MasterJenisPermintaanFilterOptionResponse> =
        masterDataSource.getJenisPermintaanFilterOptionList(id_user)

    override fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse> =
        masterDataSource.getPenugasanOptionList()

    override fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse> =
        masterDataSource.getStatusPenugasanOptionList()

    override fun getKodePekerjaanList(id: String): Observable<MasterKodePekerjaanResponse> =
        masterDataSource.getKodePekerjaanList(id)

    override fun getItemCodeLlist(id: String, keyword: String): Observable<MasterItemCodeResponse> =
        masterDataSource.getItemCodeLlist(id, keyword)

}