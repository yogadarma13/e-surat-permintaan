package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterCC
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterOption
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterPersyaratan
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterUOM

class SharedMasterData {

    val isAllMasterObservableResponseComplete: MutableLiveData<Boolean> = MutableLiveData()

    private val costCodeList: MutableLiveData<List<DataMasterCC?>?> = MutableLiveData()
    private val uomList: MutableLiveData<List<DataMasterUOM?>?> = MutableLiveData()
    private val persyaratanList: MutableLiveData<List<DataMasterPersyaratan?>?> = MutableLiveData()
    private val penugasanList: MutableLiveData<List<DataMasterOption?>?> = MutableLiveData()
    private val statusPenugasanList: MutableLiveData<List<DataMasterOption?>?> = MutableLiveData()

    private val statusOptionList: MutableLiveData<List<DataMasterOption?>?> =
        MutableLiveData()
    private val jenisDataOptionList: MutableLiveData<List<DataMasterOption?>?> =
        MutableLiveData()
    private val proyekOptionList: MutableLiveData<List<DataMasterOption?>?> =
        MutableLiveData()
    private val jenisPermintaanOptionList: MutableLiveData<List<DataMasterOption?>?> =
        MutableLiveData()

    private val onNotifikasiReceived: MutableLiveData<String> = MutableLiveData()

    fun getCostCodeList(): MutableLiveData<List<DataMasterCC?>?> = costCodeList

    fun setCostCodeList(list: List<DataMasterCC?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (costCodeList.value != null) return
        costCodeList.value = list
    }

    fun getUomList(): MutableLiveData<List<DataMasterUOM?>?> = uomList

    fun setUomList(list: List<DataMasterUOM?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (uomList.value != null) return
        uomList.value = list
    }

    fun getPersyaratanList(): MutableLiveData<List<DataMasterPersyaratan?>?> = persyaratanList

    fun setPersyaratanList(list: List<DataMasterPersyaratan?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (persyaratanList.value != null) return
        persyaratanList.value = list
    }

    fun getPenugasanList(): MutableLiveData<List<DataMasterOption?>?> = penugasanList

    fun setPenugasanList(list: List<DataMasterOption?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (penugasanList.value != null) return
        penugasanList.value = list
    }

    fun getStatusPenugasanList(): MutableLiveData<List<DataMasterOption?>?> = statusPenugasanList

    fun setStatusPenugasanList(list: List<DataMasterOption?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (statusPenugasanList.value != null) return
        statusPenugasanList.value = list
    }

    fun getStatusFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        statusOptionList

    fun setStatusFilterOptionList(list: List<DataMasterOption?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (statusOptionList.value != null) return
        statusOptionList.value = list
    }

    fun getJenisDataFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        jenisDataOptionList

    fun setJenisDataFilterOptionList(list: List<DataMasterOption?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (jenisDataOptionList.value != null) return
        jenisDataOptionList.value = list
    }

    fun getProyekFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        proyekOptionList

    fun setProyekFilterOptionList(list: List<DataMasterOption?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (proyekOptionList.value != null) return
        proyekOptionList.value = list
    }

    fun getJenisPermintaanFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        jenisPermintaanOptionList

    fun setJenisPermintaanFilterOptionList(list: List<DataMasterOption?>?) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (jenisPermintaanOptionList.value != null) return
        jenisPermintaanOptionList.value = list
    }

    fun getOnNotifikasiReceived(): MutableLiveData<String> = onNotifikasiReceived

    fun setOnNotifikasiReceived(idSp: String) {
        // ini untuk menghindari data dipopulate lebih dari sekali (duplikasi)
        if (onNotifikasiReceived.value != null) return
        onNotifikasiReceived.value = idSp
    }

}