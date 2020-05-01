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

    override fun getStatusPermintaanList(): Observable<MasterStatusPermintaanResponse> =
        masterDataSource.getStatusPermintaanList()

    override fun getJenisDataPermintaanList(): Observable<MasterJenisDataPermintaanResponse> =
        masterDataSource.getJenisDataPermintaanList()

}