package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.domain.entities.responses.*

class SharedViewModel : ViewModel() {

    private val costCodeList: MutableLiveData<List<DataMasterCC?>?> = MutableLiveData()
    private val uomList: MutableLiveData<List<DataMasterUOM?>?> = MutableLiveData()
    private val persyaratanList: MutableLiveData<List<DataMasterPersyaratan?>?> = MutableLiveData()

    private val statusFilterOptionList: MutableLiveData<List<DataMasterFilterOption?>?> =
        MutableLiveData()
    private val jenisDataFilterOptionList: MutableLiveData<List<DataMasterFilterOption?>?> =
        MutableLiveData()
    private val proyekFilterOptionList: MutableLiveData<List<DataMasterFilterOption?>?> =
        MutableLiveData()
    private val jenisPermintaanFilterOptionList: MutableLiveData<List<DataMasterFilterOption?>?> =
        MutableLiveData()

    private val onNotifikasiReceived: MutableLiveData<String> = MutableLiveData()

    fun getCostCodeList(): MutableLiveData<List<DataMasterCC?>?> = costCodeList

    fun setCostCodeList(list: List<DataMasterCC?>?) {
        costCodeList.value = list
    }

    fun getUomList(): MutableLiveData<List<DataMasterUOM?>?> = uomList

    fun setUomList(list: List<DataMasterUOM?>?) {
        uomList.value = list
    }

    fun getPersyaratanList(): MutableLiveData<List<DataMasterPersyaratan?>?> = persyaratanList

    fun setPersyaratanList(list: List<DataMasterPersyaratan?>?) {
        persyaratanList.value = list
    }

    fun getStatusFilterOptionList(): MutableLiveData<List<DataMasterFilterOption?>?> =
        statusFilterOptionList

    fun setStatusFilterOptionList(list: List<DataMasterFilterOption?>?) {
        statusFilterOptionList.value = list
    }

    fun getJenisDataFilterOptionList(): MutableLiveData<List<DataMasterFilterOption?>?> =
        jenisDataFilterOptionList

    fun setJenisDataFilterOptionList(list: List<DataMasterFilterOption?>?) {
        jenisDataFilterOptionList.value = list
    }

    fun getProyekFilterOptionList(): MutableLiveData<List<DataMasterFilterOption?>?> =
        proyekFilterOptionList

    fun setProyekFilterOptionList(list: List<DataMasterFilterOption?>?) {
        proyekFilterOptionList.value = list
    }

    fun getJenisPermintaanFilterOptionList(): MutableLiveData<List<DataMasterFilterOption?>?> =
        jenisPermintaanFilterOptionList

    fun setJenisPermintaanFilterOptionList(list: List<DataMasterFilterOption?>?) {
        jenisPermintaanFilterOptionList.value = list
    }

    fun getOnNotifikasiReceived(): MutableLiveData<String> = onNotifikasiReceived

    fun setOnNotifikasiReceived(idSp: String) {
        onNotifikasiReceived.value = idSp
    }

}