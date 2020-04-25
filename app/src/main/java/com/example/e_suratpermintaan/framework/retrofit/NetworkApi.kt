package com.example.e_suratpermintaan.framework.retrofit

import com.e_suratpermintaan.core.domain.entities.requests.*
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkApi {

    // https://stackoverflow.com/questions/44453175/why-retrofit2-works-with-field-and-not-body-for-post-requests/44453303#44453303
    // https://www.semicolonworld.com/question/44764/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request

    // Dokumentasi setingan postman untuk perbedaan @Body dan @Field Retrofit
    // https://gist.github.com/Reinhardjs/0a0d281618469ec1399518e71b42aabb

    @POST("login")
    fun loginUser(
        @Body login: Login
    ): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("profile")
    fun profileUser(
        @Field("id_user") id_user: String
    ): Observable<ProfileResponse>

    // ================================= SURAT PERMINTAAN =======================================
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

    @POST("detail")
    fun getDetailSP(
        @Body detailSP: DetailSP
    ): Observable<DetailSPResponse>

    @POST("create_sp")
    fun createSP(
        @Body createSP: CreateSP
    ) : Observable<CreateSPResponse>

    @Multipart
    @POST("edit_sp")
    fun editSP(
        @Part("id") id: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("id_user") id_user: RequestBody
    ): Observable<EditSPResponse>

    @FormUrlEncoded
    @POST("delete_sp")
    fun deleteSP(
        @Field("id") id: String
    ): Observable<DeleteSPResponse>

    @POST("ajukan_sp")
    fun ajukanSP(
        @Body ajukanSP: AjukanSP
    ): Observable<AjukanSPResponse>

    @POST("verifikasi_sp")
    fun verifikasiSP(
        @Body verifikasiSP: VerifikasiSP
    ) : Observable<VerifikasiSPResponse>

    @POST("batalkan_sp")
    fun batalkanSP(
        @Body batalkanSP: BatalkanSP
    ): Observable<BatalkanSPResponse>

    @FormUrlEncoded
    @POST("history_sp")
    fun getHistorySP(
        @Field("id_sp") id_dp: String
    ): Observable<HistorySPResponse>


    // =================================== MASTER ===============================================
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

    @FormUrlEncoded
    @POST("datauom")
    fun getMasterUom(
        @Field("id") id: String
    ): Observable<MasterUOMResponse>

    // ================================= SURAT PERMINTAAN (ITEM) ================================
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

    @FormUrlEncoded
    @POST("delete_item_sp")
    fun deleteItemSP(
        @Field("id") id: String
    ): Observable<DeleteItemSPResponse>

    // ================================= FILE LAMPIRAN ==========================================
    @Multipart
    @POST("tambah_file")
    fun createFileLampiran(
        @Part("id") id: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part("file") file: MultipartBody.Part
    ): Observable<CreateFileLampiranResponse>

    @Multipart
    @POST("edit_file")
    fun updateFileLampiran(
        @Part("keterangan") keterangan: RequestBody,
        @Part("file") file: MultipartBody.Part,
        @Part("id_file") id_file: RequestBody
    ): Observable<EditFileLampiranResponse>

    @FormUrlEncoded
    @POST("delete_file")
    fun deleteFileLampiran(
        @Field("id_file") id_file: String
    ): Observable<DeleteFileLampiranResponse>

    // ================================= NOTIFIKASI =============================================
    @FormUrlEncoded
    @POST("notifikasi")
    fun getNotifikasi(
        @Field("id_user") id_user: String
    ): Observable<NotifikasiResponse>

    @POST("read_notifikasi")
    fun readNotifikasi(
        @Body readNotifikasi: ReadNotifikasi
    ): Observable<ReadNotifikasiResponse>

}