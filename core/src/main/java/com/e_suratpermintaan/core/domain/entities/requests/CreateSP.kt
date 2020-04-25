package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateSP(
    @SerializedName("id_proyek")
    @Expose
    val id_proyek: String?,

    @SerializedName("jenis")
    @Expose
    val jenis: String?,

    @SerializedName("id_user")
    @Expose
    val id_user: String?
)