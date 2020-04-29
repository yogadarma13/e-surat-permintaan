package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class BatalkanSP (

    @field:SerializedName("id_user")
    val id_user: Int,

    @field:SerializedName("id")
    val id: Int
)