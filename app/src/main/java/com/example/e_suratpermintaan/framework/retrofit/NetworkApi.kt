package com.example.e_suratpermintaan.framework.retrofit

import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.requests.Login
import com.e_suratpermintaan.core.domain.entities.responses.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface NetworkApi {

    // https://www.semicolonworld.com/question/44764/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request

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

    @FormUrlEncoded
    @POST("detail")
    fun getDetailSP(
        @Field("id_sp") id_sp: String,
        @Field("id_user") id_user: String
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
        @Part("id_user") id_user: RequestBody
    ): Observable<EditSPResponse>

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
        @Field("id_user") id_user: String,
        @Field("id") id: String,
        @Field("status") status: String,
        @Field("catatan") catatan: String
    ): Observable<VerifikasiSPResponse>

    @FormUrlEncoded
    @POST("batalkan_sp")
    fun batalkanSP(
        @Field("id_user") id_user: String,
        @Field("id") id: String
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
    @FormUrlEncoded
    @POST("create_item_sp")
    fun createItemSP(
        @Field("kode") kode: String,
        @Field("kode_pekerjaan") kode_pekerjaan: String,
        @Field("id_barang") id_barang: String,
        @Field("id_satuan") id_satuan: String,
        @Field("qty") qty: String,
        @Field("fungsi") fungsi: String,
        @Field("target") target: String,
        @Field("keterangan") keterangan: String,
        @Field("kapasitas") kapasitas: String,
        @Field("merk") merk: String,
        @Field("waktu_pemakaian") waktu_pemakaian: String,
        @Field("waktu_pelaksanaan") waktu_pelaksanaan: String,
        @Field("persyaratan[]") persyaratan: ArrayList<String>,
        @Field("id_user") id_user: String
    ): Observable<CreateItemSPResponse>

    @FormUrlEncoded
    @POST("detail_item_sp")
    fun detailItemSP(
        @Field("id") id: String
    ): Observable<DetailItemSPResponse>

    @FormUrlEncoded
    @POST("edit_item_sp")
    fun updateItemSP(
        @Field("kode") kode: String,
        @Field("kode_pekerjaan") kode_pekerjaan: String,
        @Field("id_barang") id_barang: String,
        @Field("id_satuan") id_satuan: String,
        @Field("qty") qty: String,
        @Field("fungsi") fungsi: String,
        @Field("target") target: String,
        @Field("keterangan") keterangan: String,
        @Field("kapasitas") kapasitas: String,
        @Field("merk") merk: String,
        @Field("waktu_pemakaian") waktu_pemakaian: String,
        @Field("waktu_pelaksanaan") waktu_pelaksanaan: String,
        @Field("persyaratan[]") persyaratan: ArrayList<String>,
        @Field("id_user") id_user: String,
        @Field("id_sp") id_sp: String
    ): Observable<EditItemSPResponse>

    @FormUrlEncoded
    @POST("delete_item_sp")
    fun deleteItemSP(
        @Field("id") id: String
    ): Observable<DeleteItemSPResponse>

    // ================================= FILE LAMPIRAN ==========================================
    @FormUrlEncoded
    @POST("tambah_file")
    fun createFileLampiran(
        @Field("id") id: RequestBody,
        @Field("keterangan") keterangan: RequestBody,
        @Field("file") file: MultipartBody.Part
    ): Observable<CreateFileLampiranResponse>

    @FormUrlEncoded
    @POST("edit_file")
    fun updateFileLampiran(
        @Field("keterangan") keterangan: RequestBody,
        @Field("file") file: MultipartBody.Part,
        @Field("id_file") id_file: RequestBody
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

    @FormUrlEncoded
    @POST("read_notifikasi")
    fun readNotifikasi(
        @Field("id_user") id_user: String,
        @Field("id") id: String
    ): Observable<ReadNotifikasiResponse>

}