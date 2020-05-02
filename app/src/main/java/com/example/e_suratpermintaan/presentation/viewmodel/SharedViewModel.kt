package com.example.e_suratpermintaan.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e_suratpermintaan.core.domain.entities.responses.*

class SharedViewModel : ViewModel() {

    val isAllMasterObservableResponseComplete:MutableLiveData<Boolean> = MutableLiveData()

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

    fun getPenugasanList(): MutableLiveData<List<DataMasterOption?>?> = penugasanList

    fun setPenugasanList(list: List<DataMasterOption?>?) {
        penugasanList.value = list
    }

    fun getStatusPenugasanList(): MutableLiveData<List<DataMasterOption?>?> = statusPenugasanList

    fun setStatusPenugasnList(list: List<DataMasterOption?>?) {
        statusPenugasanList.value = list
    }

    fun getStatusFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        statusOptionList

    fun setStatusFilterOptionList(list: List<DataMasterOption?>?) {
        statusOptionList.value = list
    }

    fun getJenisDataFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        jenisDataOptionList

    fun setJenisDataFilterOptionList(list: List<DataMasterOption?>?) {
        jenisDataOptionList.value = list
    }

    fun getProyekFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        proyekOptionList

    fun setProyekFilterOptionList(list: List<DataMasterOption?>?) {
        proyekOptionList.value = list
    }

    fun getJenisPermintaanFilterOptionList(): MutableLiveData<List<DataMasterOption?>?> =
        jenisPermintaanOptionList

    fun setJenisPermintaanFilterOptionList(list: List<DataMasterOption?>?) {
        jenisPermintaanOptionList.value = list
    }

    fun getOnNotifikasiReceived(): MutableLiveData<String> = onNotifikasiReceived

    fun setOnNotifikasiReceived(idSp: String) {
        onNotifikasiReceived.value = idSp
    }

}