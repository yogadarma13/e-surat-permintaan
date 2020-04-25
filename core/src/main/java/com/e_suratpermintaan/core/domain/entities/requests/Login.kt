package com.e_suratpermintaan.core.domain.entities.requests

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("token")
    val token: String
)