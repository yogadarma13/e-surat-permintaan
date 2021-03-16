package com.e_suratpermintaan.core.domain.entities.responses

import com.google.gson.annotations.SerializedName

data class DataMaster(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("value")
    val value: String? = null,

    @field:SerializedName("option")
    val option: String? = null
) {
    override fun toString(): String = option.toString()
}
