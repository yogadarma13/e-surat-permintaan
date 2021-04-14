package com.example.e_suratpermintaan.presentation.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.DialogEditItemBinding
import com.example.e_suratpermintaan.databinding.ItemSimpleCheckboxBinding
import com.example.e_suratpermintaan.databinding.ItemSimpleRowBinding
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPA
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPB
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPS
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseFilterableAdapter
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.PersyaratanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.*
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EditItemDialog(
    private val activity: EditSuratPermintaanActivity,
    private val sharedMasterData: SharedMasterData,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel,
    private val masterViewModel: MasterViewModel
) {

    companion object {
        const val VISIBLE = 1
    }

    private var alertDialogEdit: AlertDialog
    private var datePicker: MaterialDatePicker<Long>

    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder, ItemSimpleRowBinding>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder, ItemSimpleRowBinding>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder, ItemSimpleRowBinding>
    private lateinit var kategoriAdapter: BaseFilterableAdapter<KategoriViewHolder, ItemSimpleRowBinding>

    private lateinit var statusPenugasanAdapter: BaseAdapter<StatusPenugasanViewHolder, ItemSimpleRowBinding>
    private lateinit var persyaratanAdapter: BaseAdapter<PersyaratanViewHolder, ItemSimpleCheckboxBinding>

    private var dialogRootView: DialogEditItemBinding =
        DialogEditItemBinding.inflate(LayoutInflater.from(activity), null, false)

    private lateinit var userRoleId: String

    private lateinit var selectedItemId: String

    private var allKodePekerjaan: List<DataMaster?>? = null
    private var allKategori: List<DataMaster?>? = null
    private var allSatuan: List<DataMaster?>? = null

    init {
        // https://stackoverflow.com/questions/445731372/disabling-android-o-auto-fill-service-for-an-application/45733114
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogRootView.root.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }

        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView.root)

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                .setTitle("Edit Item")
        alertDialogEdit = alertDialogBuilder.create()

        // Ini dipakai biar supaya pas keyboard showup, gak ngepush view dialog
        preventKeyboardFromPushingViews(alertDialogEdit)

        val builder = MaterialDatePicker.Builder.datePicker()
        datePicker = builder.build()

        hideAllRecyclerViews()
        setupRecyclerViews()
        populateAdapters()
        setupDatePickerListener()
        setupTextChangeListener()
        setupOnFocusChangeListener()
    }

    private val waktuPemakaianDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.formSPA.etWaktuPemakaian.setText(dateString)
    }

    private val waktuPelaksanaanDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.formSPS.etWaktuPelaksanaan.setText(dateString)
    }

    private val targetDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.formSPB.etTarget.setText(dateString)
    }

    fun initDialogViewEdit(dataProfile: DataProfile, dataDetailSP: DataDetailSP) {
        dialogRootView.etVolume.setText("0.0")
        setupAlertDialog(dataProfile, dataDetailSP)
        userRoleId = dataProfile.roleId.toString()
    }

    private fun setupDatePickerListener() {
        dialogRootView.formSPA.etWaktuPemakaian.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity.closeKeyboard(dialogRootView.formSPA.etWaktuPemakaian)
                datePicker.addOnPositiveButtonClickListener(waktuPemakaianDateSubmitListener)
                datePicker.show(activity.supportFragmentManager, datePicker.toString())
            }
        }

        dialogRootView.formSPA.pickWaktuPemakaian.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener(waktuPemakaianDateSubmitListener)
            datePicker.show(activity.supportFragmentManager, datePicker.toString())
        }


        dialogRootView.formSPS.etWaktuPelaksanaan.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity.closeKeyboard(dialogRootView.formSPS.etWaktuPelaksanaan)
                datePicker.addOnPositiveButtonClickListener(waktuPelaksanaanDateSubmitListener)
                datePicker.show(activity.supportFragmentManager, datePicker.toString())
            }
        }

        dialogRootView.formSPS.pickWaktuPelaksanaan.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener(waktuPelaksanaanDateSubmitListener)
            datePicker.show(activity.supportFragmentManager, datePicker.toString())
        }

        dialogRootView.formSPB.etTarget.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity.closeKeyboard(dialogRootView.formSPB.etTarget)
                datePicker.addOnPositiveButtonClickListener(targetDateSubmitListener)
                datePicker.show(activity.supportFragmentManager, datePicker.toString())
            }
        }

        dialogRootView.formSPB.pickTarget.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener(targetDateSubmitListener)
            datePicker.show(activity.supportFragmentManager, datePicker.toString())
        }
    }

    private fun populateAdapters() {
        sharedMasterData.getCostCodeList().observe(activity, {
            allKodePekerjaan = it
            it?.forEach { item ->
                ccAdapter.itemList.add(item as DataMaster)
//                jenisBarangAdapter.itemList.add(item)
            }
            ccAdapter.oldItemList = ccAdapter.itemList
            ccAdapter.notifyDataSetChanged()

//            jenisBarangAdapter.oldItemList = jenisBarangAdapter.itemList
//            jenisBarangAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getKodePekerjaanList().observe(activity, {
            allKategori = it
            it?.forEach { item ->
                kategoriAdapter.itemList.add(item as DataMaster)
            }
            kategoriAdapter.oldItemList = kategoriAdapter.itemList
            kategoriAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getUomList().observe(activity, {
            allSatuan = it
            it?.forEach { item ->
                uomAdapter.itemList.add(item as DataMaster)
            }
            uomAdapter.oldItemList = uomAdapter.itemList
            uomAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getStatusPenugasanList().observe(activity, {
            it?.forEach { item ->
                statusPenugasanAdapter.itemList.add(item as DataMaster)
            }
            statusPenugasanAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getPersyaratanList().observe(activity, {
            it?.forEach { item ->
                // harus di uncheck untuk menghilangkan data "checked" untuk
                // data yang baru saja ditambahkan sebelum ini
                // Karna sharedViewModel berubah datanya karna diset "checked"
                item?.isChecked = false

                persyaratanAdapter.itemList.add(item as DataMasterPersyaratan)
            }
            persyaratanAdapter.notifyDataSetChanged()
        })
    }

    private fun setupAlertDialog(dataProfile: DataProfile, dataDetailSP: DataDetailSP) {
        // val idSp = dataDetailSP.id
        val jenisPermintaan = dataDetailSP.jenis.toString()
        val kodeSp = dataDetailSP.kode.toString()

        when (jenisPermintaan) {
            JENIS_PERMINTAAN_SPA -> {
                dialogRootView.formSPA.root.visibility = View.VISIBLE
            }
            JENIS_PERMINTAAN_SPB -> {
                dialogRootView.formSPB.root.visibility = View.VISIBLE
            }
            JENIS_PERMINTAAN_SPS -> {
                dialogRootView.formSPS.root.visibility = View.VISIBLE
            }
        }

        if (dataDetailSP.inputKodePekerjaan == VISIBLE) {
            dialogRootView.tilKodePekerjaan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputIdBarang == VISIBLE) {
            dialogRootView.tilJenisBarang.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputQty == VISIBLE) {
            dialogRootView.tilVolume.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputIdSatuan == VISIBLE) {
            dialogRootView.tilSatuan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputKapasitas == VISIBLE) {
            dialogRootView.formSPA.tilKapasitas.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputMerk == VISIBLE) {
            dialogRootView.formSPA.tilMerk.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputWaktuPemakaian == VISIBLE) {
            dialogRootView.formSPA.tilWaktuPemakaian.visibility = View.VISIBLE
            dialogRootView.formSPA.pickWaktuPemakaian.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputFungsi == VISIBLE) {
            dialogRootView.formSPB.tilFungsi.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputTarget == VISIBLE) {
            dialogRootView.formSPB.tilTarget.visibility = View.VISIBLE
            dialogRootView.formSPB.pickTarget.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputWaktuPelaksanaan == VISIBLE) {
            dialogRootView.formSPS.tilWaktuPelaksanaan.visibility = View.VISIBLE
            dialogRootView.formSPS.pickWaktuPelaksanaan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputPersyaratan == VISIBLE) {
            dialogRootView.formSPS.tvPersyaratan.visibility = View.VISIBLE
            dialogRootView.formSPS.rvPersyaratan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputKeterangan == VISIBLE) {
            dialogRootView.formKeterangan.root.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputPenugasan == VISIBLE) {
            dialogRootView.tilStatusPenugasan.visibility = View.VISIBLE
        }

        dialogRootView.btnEdit.setOnClickListener {
            onSubmited(kodeSp, dataProfile)
        }

        alertDialogEdit.setView(dialogRootView.root)
    }

    private fun onSubmited(
        kodeSp: String,
        dataProfile: DataProfile
    ) {
        // Untuk menghilangkan focus yang ada di input field
        // dialogRootView.clearFocus()
        dialogRootView.root.performClick()

        val kodePekerjaan = dialogRootView.etKodePekerjaan.text.toString()
        val kategori = dialogRootView.etKategori.text.toString()
        val jenisBarang = dialogRootView.etJenisBarang.text.toString()
        val volume = dialogRootView.etVolume.text.toString()
        val satuan = dialogRootView.etSatuan.text.toString()
        val waktuPemakaian = dialogRootView.formSPA.etWaktuPemakaian.text.toString()

        val kapasitas = dialogRootView.formSPA.etKapasitas.text.toString()
        val merk = dialogRootView.formSPA.etMerk.text.toString()
        val fungsi = dialogRootView.formSPB.etFungsi.text.toString()
        val target = dialogRootView.formSPB.etTarget.text.toString()
        val waktuPelaksanaan = dialogRootView.formSPS.etWaktuPelaksanaan.text.toString()

        val statusPenugasanOption = dialogRootView.etStatusPenugasan.text.toString()
        val statusPenugasanValue = (statusPenugasanAdapter.itemList.find
        { (it as DataMaster).option == statusPenugasanOption } as DataMaster?)?.value.toString()

        val persyaratanList: ArrayList<String> = arrayListOf()
        persyaratanAdapter.itemList.forEach {
            val data = it as DataMasterPersyaratan
            if (data.isChecked) {
                persyaratanList.add(data.id.toString())
            }
        }

        val keterangan = dialogRootView.formKeterangan.etKeterangan.text.toString()

        val kodePekerjaanValue =
            allKodePekerjaan?.find { it?.option == kodePekerjaan.trim() }?.value ?: ""
        val kategoriValue = allKategori?.find { it?.option == kategori.trim() }?.value ?: ""
        val satuanValue = allSatuan?.find { it?.option == satuan.trim() }?.value ?: ""

//        if (kodePekerjaanValue.isNullOrEmpty()) dialogRootView.etKodePekerjaan.error =
//            "Pilih sesuai pilihan" else dialogRootView.etKodePekerjaan.error = null
//
//        if (kategoriValue.isNullOrEmpty()) dialogRootView.etKategori.error =
//            "Pilih sesuai pilihan" else dialogRootView.etKategori.error = null
//
//        if (satuanValue == null) dialogRootView.etSatuan.error =
//            "Pilih sesuai pilihan" else dialogRootView.etSatuan.error = null
//
//        if (dialogRootView.etKodePekerjaan.error != null || dialogRootView.etKategori.error != null || dialogRootView.etSatuan.error != null) {
//            activity.toastNotify("Lengkapi data terlebih dahulu")
//        } else {
        val confirmationDialog = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
            .setTitle("Perbarui Item")
            .setMessage("Apakah Anda yakin ingin memperbarui item?")
            .setPositiveButton("Ya") { _, _ ->
                val updateItemSP = UpdateItemSP(
                    kodeSp,
                    kodePekerjaanValue,
                    jenisBarang,
                    satuanValue,
                    volume,
                    fungsi,
                    target,
                    keterangan,
                    kapasitas,
                    merk,
                    waktuPemakaian,
                    waktuPelaksanaan,
                    persyaratanList,
                    statusPenugasanValue,
                    dataProfile.id!!,
                    selectedItemId,
                    kategoriValue
                )
                activity.disposable = itemSuratPermintaanViewModel.editItem(updateItemSP)
                    .subscribe(this::handleResponse, this::handleError)

                alertDialogEdit.hide()

            }.create()

        confirmationDialog.show()
//        }
    }

    private fun preventKeyboardFromPushingViews(dialog: Dialog?) {
        val window: Window? = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun hideAllRecyclerViews() {
        dialogRootView.rvKodePekerjaan.visibility = View.GONE
        dialogRootView.rvKategori.visibility = View.GONE
        dialogRootView.rvJenisBarang.visibility = View.GONE
        dialogRootView.rvSatuan.visibility = View.GONE
        dialogRootView.rvStatusPenugasan.visibility = View.GONE
    }

    private fun setupRecyclerViews() {
        // ------------------------------ INIT CC START ---------------------------------------------
        ccAdapter = BaseFilterableAdapter(ItemSimpleRowBinding::inflate, CCViewHolder::class.java)
        ccAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etKodePekerjaan.setText((item as DataMaster).option)

//            if (userRoleId != RoleConstants.CC) {
//                dialogRootView.etJenisBarang.setText(item.option)
//                dialogRootView.etSatuan.setText(item.option)
//            }

            // Perlu ini karna pas setText recyclerview suggestion nya si kodePekerjaan, jenisBarang,
            // & satuan muncul. Jadi harusnya di hide.
            hideAllRecyclerViews()

            activity.closeKeyboard(dialogRootView.etKodePekerjaan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvKodePekerjaan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvKodePekerjaan.adapter = ccAdapter
        // ------------------------------ INIT CC END ---------------------------------------------


        // ------------------------------ INIT KATEGORI START ------------------------------------------
        kategoriAdapter =
            BaseFilterableAdapter(ItemSimpleRowBinding::inflate, KategoriViewHolder::class.java)
        kategoriAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etKategori.setText((item as DataMaster).option)
            dialogRootView.rvKategori.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etSatuan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvKategori.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvKategori.adapter = kategoriAdapter
        // ------------------------------ INIT KATEGORI END --------------------------------------------


        // ------------------------------ INIT JENIS START ----------------------------------------
        jenisBarangAdapter =
            BaseFilterableAdapter(ItemSimpleRowBinding::inflate, JenisBarangViewHolder::class.java)
        jenisBarangAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etVolume.requestFocus()
            dialogRootView.etJenisBarang.setText((item as DataMaster).option)
            dialogRootView.rvJenisBarang.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etJenisBarang)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvJenisBarang.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvJenisBarang.adapter = jenisBarangAdapter
        // ------------------------------ INIT JENIS END ------------------------------------------


        // ------------------------------ INIT UOM START ------------------------------------------
        uomAdapter = BaseFilterableAdapter(ItemSimpleRowBinding::inflate, UomViewHolder::class.java)
        uomAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etSatuan.setText((item as DataMaster).option)
            dialogRootView.rvSatuan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etSatuan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvSatuan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvSatuan.adapter = uomAdapter
        // ------------------------------ INIT UOM END --------------------------------------------


        // -------------------------- INIT STATUS PENUGASAN START ---------------------------------
        statusPenugasanAdapter =
            BaseAdapter(ItemSimpleRowBinding::inflate, StatusPenugasanViewHolder::class.java)
        statusPenugasanAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etStatusPenugasan.setText((item as DataMaster).option)
            dialogRootView.rvStatusPenugasan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etStatusPenugasan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvStatusPenugasan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvStatusPenugasan.adapter = statusPenugasanAdapter
        // --------------------------- INIT STATUS PENUGASAN END ----------------------------------


        // ----------------------------- INIT PERSYARATAN START -----------------------------------
        persyaratanAdapter =
            BaseAdapter(ItemSimpleCheckboxBinding::inflate, PersyaratanViewHolder::class.java)
        dialogRootView.formSPS.rvPersyaratan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.formSPS.rvPersyaratan.adapter = persyaratanAdapter
        // ----------------------------- INIT PERSYARATAN END -----------------------------------
    }

    @SuppressLint("CheckResult")
    private fun setupTextChangeListener() {
        dialogRootView.etKodePekerjaan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (dialogRootView.etKodePekerjaan.isFocused) {
//                    dialogRootView.etKodePekerjaan.error = null
                    ccAdapter.filter.filter(s)
                    dialogRootView.rvKodePekerjaan.visibility = View.VISIBLE
                }
            }
        })

        dialogRootView.etKategori.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (dialogRootView.etKategori.isFocused) {
//                    dialogRootView.etKategori.error = null
                    kategoriAdapter.filter.filter(s)
                    dialogRootView.rvKategori.visibility = View.VISIBLE
                }
            }
        })

//        dialogRootView.etJenisBarang.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                jenisBarangAdapter.filter.filter(s)
//                dialogRootView.rvJenisBarang.visibility = View.VISIBLE
//            }
//        })

        dialogRootView.etJenisBarang.textChangeEvents().skipInitialValue()
            .debounce(2, TimeUnit.SECONDS).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                activity.disposable = masterViewModel.getItemCodeLlist("all", it.text.toString())
                    .subscribe(
                        this@EditItemDialog::handleResponse,
                        this@EditItemDialog::handleError
                    )
            }

        dialogRootView.etSatuan.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (dialogRootView.etSatuan.isFocused) {
//                    dialogRootView.etSatuan.error = null
                    uomAdapter.filter.filter(s)
                    dialogRootView.rvSatuan.visibility = View.VISIBLE
                }
            }
        })

    }

    private fun setupOnFocusChangeListener() {
        dialogRootView.etKodePekerjaan.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialogRootView.rvKodePekerjaan.visibility = View.VISIBLE
            } else {
                dialogRootView.rvKodePekerjaan.visibility = View.GONE
                activity.closeKeyboard(dialogRootView.etKodePekerjaan)
            }
        }

        dialogRootView.etKategori.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialogRootView.rvKategori.visibility = View.VISIBLE
            } else {
                dialogRootView.rvKategori.visibility = View.GONE
                activity.closeKeyboard(dialogRootView.etKodePekerjaan)
            }
        }

//        dialogRootView.etKategori.setOnClickListener {
//            dialogRootView.rvKategori.visibility = View.VISIBLE
//            activity.closeKeyboard(dialogRootView.etKodePekerjaan)
//        }

        dialogRootView.etJenisBarang.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialogRootView.rvJenisBarang.visibility = View.VISIBLE
            } else {
                dialogRootView.rvJenisBarang.visibility = View.GONE
                activity.closeKeyboard(dialogRootView.etJenisBarang)
            }
        }

        dialogRootView.etSatuan.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialogRootView.rvSatuan.visibility = View.VISIBLE
            } else {
                dialogRootView.rvSatuan.visibility = View.GONE
                activity.closeKeyboard(dialogRootView.etSatuan)
            }
        }

        dialogRootView.etStatusPenugasan.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialogRootView.rvStatusPenugasan.visibility = View.VISIBLE
            } else {
                dialogRootView.rvStatusPenugasan.visibility = View.GONE
                activity.closeKeyboard(dialogRootView.etStatusPenugasan)
            }
        }

//        dialogRootView.etStatusPenugasan.setOnClickListener {
//            dialogRootView.rvStatusPenugasan.visibility = View.VISIBLE
//        }
    }

    fun show(itemsDetailSP: ItemsDetailSP) {
        dialogRootView.root.clearFocus()

        selectedItemId = itemsDetailSP.id.toString()

        dialogRootView.etKodePekerjaan.setText(itemsDetailSP.kodePekerjaan)
        dialogRootView.etKategori.setText(itemsDetailSP.kategori)
        dialogRootView.etJenisBarang.setText(itemsDetailSP.idBarang)
        dialogRootView.etVolume.setText(itemsDetailSP.qty)
        dialogRootView.etSatuan.setText(itemsDetailSP.idSatuan)

        dialogRootView.formSPA.etKapasitas.setText(itemsDetailSP.kapasitas)
        dialogRootView.formSPA.etMerk.setText(itemsDetailSP.merk)
        dialogRootView.formSPA.etWaktuPemakaian.setText(itemsDetailSP.waktuPemakaian)

        dialogRootView.formSPB.etFungsi.setText(itemsDetailSP.fungsi)
        dialogRootView.formSPB.etTarget.setText(itemsDetailSP.target)
        dialogRootView.formSPS.etWaktuPelaksanaan.setText(itemsDetailSP.waktuPelaksanaan)

        dialogRootView.etStatusPenugasan.setText(itemsDetailSP.penugasan)
        dialogRootView.formKeterangan.etKeterangan.setText("")

        alertDialogEdit.show()
        hideAllRecyclerViews()

        persyaratanAdapter.itemList.forEach { item ->
            val data = item as DataMasterPersyaratan
            data.isChecked = false

            itemsDetailSP.persyaratan?.forEach {
                if (it?.persyaratan.equals(data.id)) {
                    data.isChecked = true
                }
            }
        }

        persyaratanAdapter.notifyDataSetChanged()
    }

    private fun handleResponse(response: Any) {
        when (response) {
            is MasterItemCodeResponse -> {
                jenisBarangAdapter.itemList.clear()
                response.data?.forEach { item ->
                    jenisBarangAdapter.itemList.add((item as DataMaster))
                }
                jenisBarangAdapter.oldItemList = jenisBarangAdapter.itemList
                jenisBarangAdapter.notifyDataSetChanged()
            }

            is EditItemSPResponse -> {
                activity.handleResponse(response)
            }
        }
    }

    private fun handleError(error: Throwable) {
        jenisBarangAdapter.itemList.clear()
        jenisBarangAdapter.oldItemList = jenisBarangAdapter.itemList
        jenisBarangAdapter.notifyDataSetChanged()

        activity.handleError(error)
    }

}