package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class MasterDataSourceImpl(private val networkApi: NetworkApi) : MasterDataSource {

//    override fun getProyekList(idUser: String): Observable<MasterProyekResponse> =
//        networkApi.getMasterProyek(idUser)

    override fun getJenisList(idUser: String): Observable<MasterJenisResponse> =
        networkApi.getMasterJenisSP(idUser)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        networkApi.getMasterCostCode(id)

    override fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse> =
        networkApi.getMasterPersyaratan(id)

    override fun getUomList(id: String): Observable<MasterUOMResponse> =
        networkApi.getMasterUom(id)

    override fun getStatusFilterOptionList(idUser: String): Observable<MasterStatusFilterOptionResponse> =
        networkApi.getMasterStatusFilterOptionList(idUser)

//    override fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse> =
//        networkApi.getMasterJenisDataFilterOptionList()

    override fun getProyekFilterOptionList(idUser: String): Observable<MasterProyekFilterOptionResponse> =
        networkApi.getMasterProyekFilterOptionList(idUser)

    override fun getJenisPermintaanFilterOptionList(idUser: String): Observable<MasterJenisPermintaanFilterOptionResponse> =
        networkApi.getMasterJenisFilterOptionList(idUser)

    override fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse> =
        networkApi.getMasterPenugasanOptionList()

    override fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse> =
        networkApi.getMasterStatusPenugasanOptionList()

    override fun getKodePekerjaanList(id: String): Observable<MasterKodePekerjaanResponse> =
        networkApi.getMasterKodePekerjaan(id)

    override fun getItemCodeLlist(id: String, keyword: String): Observable<MasterItemCodeResponse> =
        networkApi.getMasterItemCode(id, keyword)
}