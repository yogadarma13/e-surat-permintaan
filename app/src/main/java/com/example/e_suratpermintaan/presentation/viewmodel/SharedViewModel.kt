package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.domain.entities.responses.*

class SharedViewModel : ViewModel() {

    private val costCodeList: MutableLiveData<List<DataMasterCC?>?> = MutableLiveData()
    private val uomList: MutableLiveData<List<DataMasterUOM?>?> = MutableLiveData()
    private val persyaratanList: MutableLiveData<List<DataMasterPersyaratan?>?> = MutableLiveData()
    private val statusPermintaanList: MutableLiveData<List<DataMasterStatusPermintaan?>?> =
        MutableLiveData()
    private val jenisDataPermintaanList: MutableLiveData<List<DataMasterJenisDataPermintaan?>?> =
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

    fun getStatusPermintaanList(): MutableLiveData<List<DataMasterStatusPermintaan?>?> =
        statusPermintaanList

    fun setStatusPermintaanList(list: List<DataMasterStatusPermintaan?>?) {
        statusPermintaanList.value = list
    }

    fun getJenisDataPermintaanList(): MutableLiveData<List<DataMasterJenisDataPermintaan?>?> =
        jenisDataPermintaanList

    fun setJenisDataPermintaanList(list: List<DataMasterJenisDataPermintaan?>?) {
        jenisDataPermintaanList.value = list
    }

    fun getOnNotifikasiReceived(): MutableLiveData<String> = onNotifikasiReceived

    fun setOnNotifikasiReceived(idSp: String) {
        onNotifikasiReceived.value = idSp
    }

}