package com.example.e_suratpermintaan.framework.datasourceimpl

import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterProyekResponse
import com.example.e_suratpermintaan.framework.retrofit.NetworkApi
import io.reactivex.rxjava3.core.Observable

class MasterDataSourceImpl(private val networkApi: NetworkApi) : MasterDataSource {

    override fun getProyekList(id_user: String): Observable<MasterProyekResponse> =
        networkApi.getMasterProyek(id_user)

    override fun getJenisList(id_user: String): Observable<MasterJenisResponse> =
        networkApi.getMasterJenis(id_user)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        networkApi.getMasterCostCode(id)

}