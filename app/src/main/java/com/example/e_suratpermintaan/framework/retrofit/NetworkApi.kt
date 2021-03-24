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

    @Multipart
    @POST("edit_profile")
    fun editProfile(
        @Part("id") id: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password_last") passwordLast: RequestBody,
        @Part("passwordNew") passwordNew: RequestBody,
        @Part("name") name: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part file: MultipartBody.Part?,
        @Part ttd: MultipartBody.Part?
    ): Observable<EditProfileResponse>

    // ================================= SURAT PERMINTAAN =======================================
    @GET("dataall")
    fun getDataAll(
        @Query("id_user") idUser: String,
        @Query("id_proyek") idProyek: String,
        @Query("id_jenis") idJenis: String,
        @Query("id_status") idStatus: String
    ): Observable<DataAllResponse>

    @GET("mydata")
    fun getMyData(
        @Query("id_user") idUser: String,
        @Query("id_proyek") idProyek: String,
        @Query("id_jenis") idJenis: String
    ): Observable<MyDataResponse>

    @GET("detail")
    fun getDetailSP(
        @Query("id_sp") idSp: String,
        @Query("id_user") idUser: String
    ): Observable<DetailSPResponse>

    @POST("create_sp")
    fun createSP(
        @Body createSP: CreateSP
    ): Observable<CreateSPResponse>

    @Multipart
    @POST("edit_sp")
    fun editSP(
        @Part("id") id: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("id_user") idUser: RequestBody
    ): Observable<EditSPResponse>

    @FormUrlEncoded
    @POST("delete_sp")
    fun deleteSP(
        @Field("id") id: String
    ): Observable<DeleteSPResponse>

    @Multipart
    @POST("ajukan_sp")
    fun ajukanSP(
        @Part("id_user") idUser: RequestBody,
        @Part("id") id: RequestBody,
        @Part file: MultipartBody.Part
    ): Observable<AjukanSPResponse>

    @Multipart
    @POST("verifikasi_sp")
    fun verifikasiSP(
        @Part("id_user") idUser: RequestBody,
        @Part("id") id: RequestBody,
        @Part("status") status: RequestBody,
        @Part("catatan") note: RequestBody,
        @Part file: MultipartBody.Part
    ): Observable<VerifikasiSPResponse>

    @FormUrlEncoded
    @POST("batalkan_sp")
    fun batalkanSP(
        @Field("id_user") idUser: String,
        @Field("id") id: String
    ): Observable<BatalkanSPResponse>

    @GET("history_sp")
    fun getHistorySP(
        @Query("id_sp") idSp: String
    ): Observable<HistorySPResponse>

    @FormUrlEncoded
    @POST("edit_sp")
    fun simpanEditSP(
        @Field("id") id: String,
        @Field("id_user") idUser: String
    ): Observable<SimpanEditSPResponse>

    // =================================== MASTER ===============================================
    @GET("datastatus")
    fun getMasterStatusFilterOptionList(
        @Query("id_user") idUser: String
    ): Observable<MasterStatusFilterOptionResponse>

//    @GET("jenis_data")
//    fun getMasterJenisDataFilterOptionList(): Observable<MasterJenisDataFilterOptionResponse>

    @GET("datajenis")
    fun getMasterJenisFilterOptionList(
        @Query("id_user") idUser: String
    ): Observable<MasterJenisPermintaanFilterOptionResponse>

    @GET("dataproyek")
    fun getMasterProyekFilterOptionList(
        @Query("id_user") idUser: String
    ): Observable<MasterProyekFilterOptionResponse>

    @GET("penugasan")
    fun getMasterPenugasanOptionList(): Observable<MasterPenugasanOptionResponse>

    @GET("status_penugasan")
    fun getMasterStatusPenugasanOptionList(): Observable<MasterStatusPenugasanOptionResponse>

//    @GET("dataproyek")
//    fun getMasterProyek(
//        @Query("id_user") idUser: String
//    ): Observable<MasterProyekResponse>

    @GET("datajenissp")
    fun getMasterJenisSP(
        @Query("id_user") idUser: String
    ): Observable<MasterJenisResponse>

    @GET("datacostcode")
    fun getMasterCostCode(
        @Query("id") id: String
    ): Observable<MasterCCResponse>

    @GET("datajenisbarang")
    fun getMasterItemCode(
        @Query("id") id: String,
        @Query("keyword") keyword: String
    ): Observable<MasterItemCodeResponse>

    @GET("datapersyaratan")
    fun getMasterPersyaratan(
        @Query("id") id: String
    ): Observable<MasterPersyaratanResponse>

    @GET("datauom")
    fun getMasterUom(
        @Query("id") id: String
    ): Observable<MasterUOMResponse>

    @GET("datakodepekerjaan")
    fun getMasterKodePekerjaan(
        @Query("id") id: String
    ): Observable<MasterKodePekerjaanResponse>

    // ================================= SURAT PERMINTAAN (ITEM) ================================
    @POST("create_item_sp")
    fun createItemSP(
        @Body createItemSP: CreateItemSP
    ): Observable<CreateItemSPResponse>

    @GET("detail_item_sp")
    fun detailItemSP(
        @Query("id") id: String
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

    @POST("penugasan_item_sp")
    fun setPenugasanItemSP(
        @Body penugasanItemSP: PenugasanItemSP
    ): Observable<PenugasanItemSPResponse>

    @GET("process")
    fun processItemSP(
        @Query("id_sp") idSp: String,
        @Query("id_item") idItem: String,
        @Query("id_user") idUser: String
    ): Observable<ProcessItemSPResponse>

    @GET("un_process")
    fun unProcessItemSP(
        @Query("id_sp") idSp: String,
        @Query("id_item") idItem: String,
        @Query("id_user") idUser: String
    ): Observable<ProcessItemSPResponse>

    @FormUrlEncoded
    @POST("tolak_item")
    fun rejectItem(
        @Field("id_user") idUser: String,
        @Field("id_sp") idSp: String,
        @Field("id_item") idItem: String,
        @Field("catatan") note: String
    ): Observable<RejectItemSPResponse>

    @FormUrlEncoded
    @POST("rollback_item")
    fun rollbackItem(
        @Field("id_user") idUser: String,
        @Field("id_sp") idSp: String,
        @Field("id_item") idItem: String
    ): Observable<RollbackItemSPResponse>

    // ================================= FILE LAMPIRAN ==========================================
    @Multipart
    @POST("tambah_file")
    fun createFileLampiran(
        @Part("id") id: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part file: MultipartBody.Part
    ): Observable<CreateFileLampiranResponse>

    @Multipart
    @POST("edit_file")
    fun updateFileLampiran(
        @Part("keterangan") keterangan: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("id_file") idFile: RequestBody
    ): Observable<EditFileLampiranResponse>

    @FormUrlEncoded
    @POST("delete_file")
    fun deleteFileLampiran(
        @Field("id_file") idFile: String
    ): Observable<DeleteFileLampiranResponse>

    // ================================= NOTIFIKASI =============================================
    @GET("notifikasi")
    fun getNotifikasi(
        @Query("id_user") idUser: String
    ): Observable<NotifikasiResponse>

    @POST("read_notifikasi")
    fun readNotifikasi(
        @Body readNotifikasi: ReadNotifikasi
    ): Observable<ReadNotifikasiResponse>

}