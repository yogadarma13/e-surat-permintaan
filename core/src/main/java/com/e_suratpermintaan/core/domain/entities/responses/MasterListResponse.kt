package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterCCResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterJenisResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterKodePekerjaanResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

//data class MasterPersyaratanResponse(
//
//    @field:SerializedName("data")
//    val data: List<DataMaster?>? = null,
//
//    @field:SerializedName("message")
//    val message: String? = null,
//
//    @field:SerializedName("status")
//    val status: Boolean? = null
//)

data class MasterProyekResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterStatusResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterUOMResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)