package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.usecases.master.*
import com.e_suratpermintaan.core.usecases.master.optionlist.GetPenugasanOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.GetStatusPenugasanOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetJenisDataFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetJenisPermintaanFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetProyekFilterOptionUseCase
import com.e_suratpermintaan.core.usecases.master.optionlist.filter.GetStatusFilterOptionUseCase
import io.reactivex.rxjava3.core.Observable

class MasterViewModel(
//    private val getProyekListUseCase: GetProyekListUseCase,
    private val getJenisListUseCase: GetJenisListUseCase,
    private val getCostCodeListUseCase: GetCostCodeListUseCase,
    private val getPersyaratanListUseCase: GetPersyaratanListUseCase,
    private val getUomListUseCase: GetUomListUseCase,
    private val getMasterStatusFilterOptionUseCase: GetStatusFilterOptionUseCase,
//    private val getMasterJenisDataFilterOptionUseCase: GetJenisDataFilterOptionUseCase,
    private val getMasterProyekFilterOptionUseCase: GetProyekFilterOptionUseCase,
    private val getMasterJenisPermintaanFilterOptionUseCase: GetJenisPermintaanFilterOptionUseCase,
    private val getMasterPenugasanOptionUseCase: GetPenugasanOptionUseCase,
    private val getMasterStatusPenugasanOptionUseCase: GetStatusPenugasanOptionUseCase,
    private val getKodePekerjaanListUseCase: GetKodePekerjaanListUseCase,
    private val getItemCodeListUseCase: GetItemCodeListUseCase
) : ViewModel(), MasterDataSource {

//    override fun getProyekList(idUser: String): Observable<MasterProyekResponse> =
//        getProyekListUseCase.invoke(idUser)

    override fun getJenisList(idUser: String): Observable<MasterJenisResponse> =
        getJenisListUseCase.invoke(idUser)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        getCostCodeListUseCase.invoke(id)

    override fun getPersyaratanList(id: String): Observable<MasterPersyaratanResponse> =
        getPersyaratanListUseCase.invoke(id)

    override fun getUomList(id: String): Observable<MasterUOMResponse> =
        getUomListUseCase.invoke(id)

    override fun getStatusFilterOptionList(idUser: String): Observable<MasterStatusFilterOptionResponse> =
        getMasterStatusFilterOptionUseCase.invoke(idUser)

//    override fun getJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse> =
//        getMasterJenisDataFilterOptionUseCase.invoke()

    override fun getProyekFilterOptionList(idUser: String): Observable<MasterProyekFilterOptionResponse> =
        getMasterProyekFilterOptionUseCase.invoke(idUser)

    override fun getJenisPermintaanFilterOptionList(idUser: String): Observable<MasterJenisPermintaanFilterOptionResponse> =
        getMasterJenisPermintaanFilterOptionUseCase.invoke(idUser)

    override fun getPenugasanOptionList(): Observable<MasterPenugasanOptionResponse> =
        getMasterPenugasanOptionUseCase.invoke()

    override fun getStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse> =
        getMasterStatusPenugasanOptionUseCase.invoke()

    override fun getKodePekerjaanList(id: String): Observable<MasterKodePekerjaanResponse> =
        getKodePekerjaanListUseCase.invoke(id)

    override fun getItemCodeLlist(id: String, keyword: String): Observable<MasterItemCodeResponse> =
        getItemCodeListUseCase.invoke(id, keyword)
}