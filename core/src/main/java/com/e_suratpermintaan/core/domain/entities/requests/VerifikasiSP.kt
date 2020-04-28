package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class VerifikasiSP(

    @field:SerializedName("id_user")
    val id_user: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("catatan")
    val catatan: String
)