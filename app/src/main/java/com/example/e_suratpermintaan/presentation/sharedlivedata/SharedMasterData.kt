package com.example.e_suratpermintaan.presentation.sharedlivedata

import androidx.lifecycle.MutableLiveData
import com.e_suratpermintaan.core.domain.entities.responses.DataMaster
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterPersyaratan

class SharedMasterData {

    val isAllMasterObservableResponseComplete: MutableLiveData<Boolean> = MutableLiveData()

    private val costCodeList: MutableLiveData<List<DataMaster?>?> = MutableLiveData()
    private val uomList: MutableLiveData<List<DataMaster?>?> = MutableLiveData()
    private val kodePekerjaanList: MutableLiveData<List<DataMaster?>?> = MutableLiveData()
    private val persyaratanList: MutableLiveData<List<DataMasterPersyaratan?>?> = MutableLiveData()
    private val penugasanList: MutableLiveData<List<DataMaster?>?> = MutableLiveData()
    private val statusPenugasanList: MutableLiveData<List<DataMaster?>?> = MutableLiveData()

    private var statusOptionList: MutableLiveData<List<DataMaster?>?> =
        MutableLiveData()
    private var jenisDataOptionList: MutableLiveData<List<DataMaster?>?> =
        MutableLiveData()
    private var proyekOptionList: MutableLiveData<List<DataMaster?>?>? =
        MutableLiveData()
    private var jenisPermintaanOptionList: MutableLiveData<List<DataMaster?>?>? =
        MutableLiveData()

    private val onNotifikasiReceived: MutableLiveData<String> = MutableLiveData()

    fun getCostCodeList(): MutableLiveData<List<DataMaster?>?> = costCodeList

    fun setCostCodeList(list: List<DataMaster?>?) {
        costCodeList.value = list
    }

    fun getUomList(): MutableLiveData<List<DataMaster?>?> = uomList

    fun setUomList(list: List<DataMaster?>?) {
        uomList.value = list
    }

    fun getKodePekerjaanList(): MutableLiveData<List<DataMaster?>?> = kodePekerjaanList

    fun setKodePekerjaanList(list: List<DataMaster?>?) {
        kodePekerjaanList.value = list
    }

    fun getPersyaratanList(): MutableLiveData<List<DataMasterPersyaratan?>?> = persyaratanList

    fun setPersyaratanList(list: List<DataMasterPersyaratan?>?) {
        persyaratanList.value = list
    }

    fun getPenugasanList(): MutableLiveData<List<DataMaster?>?> = penugasanList

    fun setPenugasanList(list: List<DataMaster?>?) {
        penugasanList.value = list
    }

    fun getStatusPenugasanList(): MutableLiveData<List<DataMaster?>?> = statusPenugasanList

    fun setStatusPenugasanList(list: List<DataMaster?>?) {
        statusPenugasanList.value = list
    }

    fun getStatusFilterOptionList(): MutableLiveData<List<DataMaster?>?> =
        statusOptionList

    fun setStatusFilterOptionList(list: List<DataMaster?>?) {
        statusOptionList.value = list
    }

    fun getJenisDataFilterOptionList(): MutableLiveData<List<DataMaster?>?> =
        jenisDataOptionList

    fun setJenisDataFilterOptionList(list: List<DataMaster?>?) {
        jenisDataOptionList.value = list
    }

    fun getProyekFilterOptionList(): MutableLiveData<List<DataMaster?>?>? =
        proyekOptionList

    fun setProyekFilterOptionList(list: List<DataMaster?>?) {
        proyekOptionList?.value = list
    }

    fun getJenisPermintaanFilterOptionList(): MutableLiveData<List<DataMaster?>?>? =
        jenisPermintaanOptionList

    fun setJenisPermintaanFilterOptionList(list: List<DataMaster?>?) {
        jenisPermintaanOptionList?.value = list
    }

    fun getOnNotifikasiReceived(): MutableLiveData<String> = onNotifikasiReceived

    fun setOnNotifikasiReceived(idSp: String) {
        onNotifikasiReceived.value = idSp
    }

}