package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.data.datasource.MasterDataSource
import com.e_suratpermintaan.core.domain.entities.responses.MasterCCResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterJenisResponse
import com.e_suratpermintaan.core.domain.entities.responses.MasterProyekResponse
import com.e_suratpermintaan.core.usecases.master.GetCostCodeListUseCase
import com.e_suratpermintaan.core.usecases.master.GetJenisListUseCase
import com.e_suratpermintaan.core.usecases.master.GetProyekListUseCase
import io.reactivex.rxjava3.core.Observable

class MasterViewModel(
    private val getProyekListUseCase: GetProyekListUseCase,
    private val getJenisListUseCase: GetJenisListUseCase,
    private val getCostCodeListUseCase: GetCostCodeListUseCase
) : ViewModel(), MasterDataSource {

    override fun getProyekList(id_user: String): Observable<MasterProyekResponse> =
        getProyekListUseCase.invoke(id_user)

    override fun getJenisList(id_user: String): Observable<MasterJenisResponse> =
        getJenisListUseCase.invoke(id_user)

    override fun getCostCodeList(id: String): Observable<MasterCCResponse> =
        getCostCodeListUseCase.invoke(id)

}