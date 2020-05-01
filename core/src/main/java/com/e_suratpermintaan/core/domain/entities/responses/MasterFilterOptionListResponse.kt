package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterJenisPermintaanFilterOptionResponse (

    @field:SerializedName("data")
    val data: List<DataMasterFilterOption?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterProyekFilterOptionResponse (

    @field:SerializedName("data")
    val data: List<DataMasterFilterOption?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterJenisDataFilterOptionResponse (

    @field:SerializedName("data")
	val data: List<DataMasterFilterOption?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)

data class MasterStatusFilterOptionResponse (

    @field:SerializedName("data")
    val data: List<DataMasterFilterOption?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class DataMasterFilterOption(

    @field:SerializedName("option")
    val option: String? = null,

    @field:SerializedName("value")
    val value: String? = null
){
	override fun toString(): String = option.toString()
}