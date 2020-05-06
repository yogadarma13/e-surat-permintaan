package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class PenugasanItemSP(
    @field:SerializedName("id_user")
    val idUser: String,

    @field:SerializedName("id_item_sp")
    val idItemSp: String,

    @field:SerializedName("kepada")
    val kepada: String
)