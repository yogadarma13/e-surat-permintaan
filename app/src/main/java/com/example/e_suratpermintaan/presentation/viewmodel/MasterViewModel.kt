package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.usecases.master.*
import io.reactivex.rxjava3.core.Observable

class MasterViewModel(
    private val getProyekListUseCase: GetProyekListUseCase,
    private val getJenisListUseCase: GetJenisListUseCase,
    private val getCostCodeListUseCase: GetCostCodeListUseCase,
    private val getPersyaratanListUseCase: GetPersyaratanListUseCase,
    private val getUomListUseCase: GetUomListUseCase,
    private val getMasterStatusPermintaanListUseCase: GetStatusPermintaanListUseCase,
    private val getmasterJenisDataPermintaanListUseCase: GetJenisDataPermintaanListUseCase
) : ViewModel(), MasterDataSource {

    override fun getProyekList(id_user: String): Observable<MasterProyekResponse> =
        getProyekListUseCase.invoke(id_user)

    override fun getJenisList(id_user: String): Observable<MasterJenisResponse> =
        getJenisListUseCase.invoke(id_user)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        getCostCodeListUseCase.invoke(id)

    override fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse> =
        getPersyaratanListUseCase.invoke(id)

    override fun getUomList(id: String): Observable<MasterUOMResponse> =
        getUomListUseCase.invoke(id)

    override fun getStatusPermintaanList(): Observable<MasterStatusPermintaanResponse> =
        getMasterStatusPermintaanListUseCase.invoke()

    override fun getJenisDataPermintaanList(): Observable<MasterJenisDataPermintaanResponse> =
        getmasterJenisDataPermintaanListUseCase.invoke()
}