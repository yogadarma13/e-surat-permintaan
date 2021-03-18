package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class MasterDataSourceImpl(private val networkApi: NetworkApi) : MasterDataSource {

    override fun getProyekList(id_user: String): Observable<MasterProyekResponse> =
        networkApi.getMasterProyek(id_user)

    override fun getJenisList(id_user: String): Observable<MasterJenisResponse> =
        networkApi.getMasterJenis(id_user)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        networkApi.getMasterCostCode(id)

    override fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse> =
        networkApi.getMasterPersyaratan(id)

    override fun getUomList(id: String): Observable<MasterUOMResponse> =
        networkApi.getMasterUom(id)

    override fun getStatusFilterOptionList(id_user: String): Observable<MasterStatusFilterOptionResponse> =
        networkApi.getMasterStatusFilterOptionList(id_user)

    override fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse> =
        networkApi.getMasterJenisDataFilterOptionList()

    override fun getProyekFilterOptionList(id_user: String): Observable<MasterProyekFilterOptionResponse> =
        networkApi.getMasterProyekFilterOptionList(id_user)

    override fun getJenisPermintaanFilterOptionList(id_user: String): Observable<MasterJenisPermintaanFilterOptionResponse> =
        networkApi.getMasterJenisFilterOptionList(id_user)

    override fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse> =
        networkApi.getMasterPenugasanOptionList()

    override fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse> =
        networkApi.getMasterStatusPenugasanOptionList()

    override fun getKodePekerjaanList(id: String): Observable<MasterKodePekerjaanResponse> =
        networkApi.getMasterKodePekerjaan(id)

    override fun getItemCodeLlist(id: String, keyword: String): Observable<MasterItemCodeResponse> =
        networkApi.getMasterItemCode(id, keyword)
}