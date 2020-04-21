package com.e_suratpermintaan.core.data.repository

import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisProyekResponse
import io.reactivex.rxjava3.core.Observable

class MasterRepository(private val masterDataSource: MasterDataSource) :
    MasterDataSource {

    override fun getProyekList(id_user: String): Observable<MasterJenisProyekResponse> =
        masterDataSource.getProyekList(id_user)

    override fun getJenisList(id_user: String): Observable<MasterJenisProyekResponse> =
        masterDataSource.getJenisList(id_user)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        masterDataSource.getCostCodeList(id)

}