package com.example.e_suratpermintaan.framework.retrofit

import com.e_suratpermintaan.core.domain.entities.requests.*
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkApi {

    @POST("login")
    fun loginUser(
        @Body login: Login
    ): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("profile")
    fun profileUser(
        @Field("id_user") id_user: String
    ): Observable<ProfileResponse>

    // Surat Permintaan
    @FormUrlEncoded
    @POST("DataAll")
    fun getDataAll(
        @Field("id_user") id_user: String
    ): Observable<DataAllResponse>

    @FormUrlEncoded
    @POST("MyData")
    fun getMyData(
        @Field("id_user") id_user: String
    ): Observable<MyDataResponse>

    @FormUrlEncoded
    @POST("detail")
    fun getDetailSP(
        @Field("id_sp") id_sp: String
    ): Observable<DetailSPResponse>

    @FormUrlEncoded
    @POST("create_sp")
    fun createSP(
        @Field("id_proyek") id_proyek: String,
        @Field("jenis") jenis : String,
        @Field("id_user") id_user : String
    ) : Observable<CreateSPResponse>

    @Multipart
    @POST("edit_sp")
    fun editSP(
        @Part("id") id : RequestBody,
        @Part file : MultipartBody.Part,
        @Part("id_user") id_user: RequestBody
    ) : Observable<EditSPResponse>

    @FormUrlEncoded
    @POST("delete_sp")
    fun deleteSP(
        @Field("id") id: String
    ): Observable<DeleteSPResponse>

    @FormUrlEncoded
    @POST("ajukan_sp")
    fun ajukanSP(
        @Field("id_user") id_user: String,
        @Field("id") id: String
    ): Observable<AjukanSPResponse>

    @POST("verifikasi_sp")
    fun verifikasiSP(
        @Body verifikasiSP: VerifikasiSP
    ) : Observable<VerifikasiSPResponse>

    @FormUrlEncoded
    @POST("batalkan_sp")
    fun batalkanSP(
        @Field("id_user") id_user: String,
        @Field("id") id: String
    ): Observable<AjukanSPResponse>


    // Master
    @FormUrlEncoded
    @POST("dataproyek")
    fun getMasterProyek(
        @Field("id_user") id_user: String
    ): Observable<MasterProyekResponse>

    @FormUrlEncoded
    @POST("datajenis")
    fun getMasterJenis(
        @Field("id_user") id_user: String
    ): Observable<MasterJenisResponse>

    @FormUrlEncoded
    @POST("datacostcode")
    fun getMasterCostCode(
        @Field("id") id: String
    ): Observable<MasterCCResponse>

    @FormUrlEncoded
    @POST("datapersyaratan")
    fun getMasterPersyaratan(
        @Field("id") id: String
    ): Observable<MasterPersyaratanResponse>

    // Surat Permintaan (Item)
    @POST("create_item_sp")
    fun createItemSP(
        @Body createItemSP: CreateItemSP
    ): Observable<CreateItemSPResponse>

    @FormUrlEncoded
    @POST("detail_item_sp")
    fun detailItemSP(
        @Field("id") id: String
    ): Observable<DetailItemSPResponse>

    @POST("edit_item_sp")
    fun updateItemSP(
        @Body updateItemSP: UpdateItemSP
    ): Observable<EditItemSPResponse>

    @POST("delete_item_sp")
    fun deleteItemSP(
        @Field("id") id: String
    ): Observable<DeleteItemSPResponse>

}