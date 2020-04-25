package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class DetailSP(

    @field:SerializedName("id_sp")
    val id_sp: String,

    @field:SerializedName("id_user")
    val id_user: String
)