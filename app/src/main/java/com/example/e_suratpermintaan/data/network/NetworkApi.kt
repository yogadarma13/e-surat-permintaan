package com.example.e_suratpermintaan.data.network

import com.example.e_suratpermintaan.data.network.requests.CreateItemSP
import com.example.e_suratpermintaan.data.network.requests.Login
import com.example.e_suratpermintaan.data.network.requests.UpdateItemSP
import com.example.e_suratpermintaan.data.network.responses.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface NetworkApi {

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("login")
    fun loginUser(
        @Body Login: Login
    ) : Observable<LoginResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("profile")
    fun profileUser(
        @Field("id_user") id_user : String
    ) : Observable<ProfileResponse>

    // Master
    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("dataproyek")
    fun getMasterProyek(
        @Field("id_user") id_user: String
    ) : Observable<MasterJenisProyekResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("datajenis")
    fun getMasterJenis(
        @Field("id_user") id_user: String
    ) : Observable<MasterJenisProyekResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("datajenis")
    fun getMasterCostCode(
        @Field("id") id: String
    ) : Observable<MasterCCResponse>

    // Surat Permintaan
    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("create_item_sp")
    fun createItemSP(
        @Body createItemSP: CreateItemSP
    ) : Observable<BaseResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("detail_item_sp")
    fun detailItemSP(
        @Field("id") id : String
    ) : Observable<DetailItemSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("edit_item_sp")
    fun updateItemSP(
        @Body updateItemSP: UpdateItemSP
    ) : Observable<BaseResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("delete_item_sp")
    fun deleteItemSP(
        @Field("id") id: String
    ) : Observable<DeleteItemSPResponse>

}