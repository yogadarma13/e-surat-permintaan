package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class MasterJenisPermintaanFilterOptionResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterProyekFilterOptionResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterStatusFilterOptionResponse(

    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterPenugasanOptionResponse(
    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class MasterStatusPenugasanOptionResponse(
    @field:SerializedName("data")
    val data: List<DataMaster?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)