package com.example.e_suratpermintaan.data.network

import com.example.e_suratpermintaan.data.network.requests.*
import com.example.e_suratpermintaan.data.network.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkApi {

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("login")
    fun loginUser(
        @Body Login: Login
    ): Observable<LoginResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("profile")
    fun profileUser(
        @Field("id_user") id_user: String
    ): Observable<ProfileResponse>

    // Surat Permintaan
    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("MyData")
    fun getMyData(
        @Field("id_user") id_user: String
    ): Observable<MyDataResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("MyData")
    fun getDataAll(
        @Field("id_user") id_user: String
    ): Observable<MyDataResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("detail")
    fun getDetailSP(
        @Field("id_sp") id_sp: String
    ): Observable<DetailSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("create_sp")
    fun createSP(
        @Body createSP: CreateSP
    ) : Observable<CreateEditSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @Multipart
    @POST("edit_sp")
    fun editSP(
        @Part("id") id : RequestBody,
        @Part file : MultipartBody.Part,
        @Part("id_user") id_user: String
    ) : Observable<CreateEditSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("delete_sp")
    fun deleteSP(
        @Field("id") id: String
    ): Observable<DeleteSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("ajukan_sp")
    fun ajukanSP(
        @Field("id_user") id_user: String,
        @Field("id") id: String
    ): Observable<AjukanSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("verifikasi_sp")
    fun verifikasiSP(
        @Body verifikasiSP: VerifikasiSP
    ) : Observable<VerifikasiSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("batalkan_sp")
    fun batalkanSP(
        @Field("id_user") id_user: String,
        @Field("id") id: String
    ): Observable<AjukanSPResponse>

    // Master
    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("dataproyek")
    fun getMasterProyek(
        @Field("id_user") id_user: String
    ): Observable<MasterJenisProyekResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("datajenis")
    fun getMasterJenis(
        @Field("id_user") id_user: String
    ): Observable<MasterJenisProyekResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("datajenis")
    fun getMasterCostCode(
        @Field("id") id: String
    ): Observable<MasterCCResponse>


    // Surat Permintaan (Item)
    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("create_item_sp")
    fun createItemSP(
        @Body createItemSP: CreateItemSP
    ): Observable<BaseResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @FormUrlEncoded
    @POST("detail_item_sp")
    fun detailItemSP(
        @Field("id") id: String
    ): Observable<DetailItemSPResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("edit_item_sp")
    fun updateItemSP(
        @Body updateItemSP: UpdateItemSP
    ): Observable<BaseResponse>

    @Headers("x-sm-key: 35d3d08c3d7b7f445ceb8e726a87b26c")
    @POST("delete_item_sp")
    fun deleteItemSP(
        @Field("id") id: String
    ): Observable<DeleteItemSPResponse>

}