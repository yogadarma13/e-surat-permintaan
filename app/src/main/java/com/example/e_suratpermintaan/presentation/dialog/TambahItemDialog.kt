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
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.DialogTambahItemBinding
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

class TambahItemDialog(
    private val activity: EditSuratPermintaanActivity,
    private val sharedMasterData: SharedMasterData,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel,
    private val masterViewModel: MasterViewModel
) {

    companion object {
        const val VISIBLE = 1
    }

    private var alertDialogTambah: AlertDialog
    private var datePicker: MaterialDatePicker<Long>

    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder, ItemSimpleRowBinding>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder, ItemSimpleRowBinding>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder, ItemSimpleRowBinding>
    private lateinit var kategoriAdapter: BaseFilterableAdapter<KategoriViewHolder, ItemSimpleRowBinding>

    private lateinit var statusPenugasanAdapter: BaseAdapter<StatusPenugasanViewHolder, ItemSimpleRowBinding>
    private lateinit var persyaratanAdapter: BaseAdapter<PersyaratanViewHolder, ItemSimpleCheckboxBinding>

    private var allKodePekerjaan: List<DataMaster?>? = null
    private var allKategori: List<DataMaster?>? = null
    private var allSatuan: List<DataMaster?>? = null

    private var dialogRootView: DialogTambahItemBinding =
        DialogTambahItemBinding.inflate(LayoutInflater.from(activity), null, false)

    init {
        // https://stackoverflow.com/questions/45731372/disabling-android-o-auto-fill-service-for-an-application/45733114
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogRootView.root.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }

        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView.root)

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                .setTitle("Tambah Item")
        alertDialogTambah = alertDialogBuilder.create()

        // Ini dipakai biar supaya pas keyboard showup, gak ngepush view dialog
        preventKeyboardFromPushingViews(alertDialogTambah)

        val builder = MaterialDatePicker.Builder.datePicker()
        datePicker = builder.build()

        hideAllRecyclerViews()
        setupRecyclerViews()
        populateAdapterList()
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

    fun initDialogViewTambah(dataProfile: DataProfile, dataDetailSP: DataDetailSP) {
        dialogRootView.etVolume.setText("0.0")
        setupAlertDialog(dataProfile, dataDetailSP)
    }

    private fun setupDatePickerListener() {
        dialogRootView.formSPA.etWaktuPemakaian.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
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

    private fun populateAdapterList() {
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

//        sharedMasterData.getItemCodeList().observe(activity, {
//            it?.forEach { item ->
//                jenisBarangAdapter.itemList.add((item as DataMaster))
//            }
//            jenisBarangAdapter.oldItemList = jenisBarangAdapter.itemList
//            jenisBarangAdapter.notifyDataSetChanged()
//        })

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

        dialogRootView.btnTambah.setOnClickListener {
            // Untuk menghilangkan focus yang ada di input field
            // dialogRootView.clearFocus()
            dialogRootView.root.performClick()

            val kodePekerjaan = dialogRootView.etKodePekerjaan.text.toString()
            val jenisBarang = dialogRootView.etJenisBarang.text.toString()
            val kategori = dialogRootView.etKategori.text.toString()
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
                allKodePekerjaan?.find { it?.option == kodePekerjaan.trim() }?.value
            val kategoriValue = allKategori?.find { it?.option == kategori.trim() }?.value
            val satuanValue = allSatuan?.find { it?.option == satuan.trim() }?.value

            if (kodePekerjaanValue.isNullOrEmpty()) dialogRootView.etKodePekerjaan.error =
                "Pilih sesuai pilihan" else dialogRootView.etKodePekerjaan.error = null

            if (kategoriValue.isNullOrEmpty()) dialogRootView.etKategori.error =
                "Pilih sesuai pilihan" else dialogRootView.etKategori.error = null

            if (satuanValue == null) dialogRootView.etSatuan.error =
                "Pilih sesuai pilihan" else dialogRootView.etSatuan.error = null

            if (dialogRootView.etKodePekerjaan.error != null || dialogRootView.etKategori.error != null || dialogRootView.etSatuan.error != null) {
                activity.toastNotify("Lengkapi data terlebih dahulu")
            } else {
                val confirmationDialog =
                    MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                        .setMessage("Apakah Anda yakin ingin menambah item?")
                        .setPositiveButton("Ya") { _, _ ->
                            val createItemSP = CreateItemSP(
                                kodeSp,
                                kodePekerjaanValue!!,
                                jenisBarang,
                                satuanValue!!,
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
                                kategoriValue!!
                            )
                            activity.disposable = itemSuratPermintaanViewModel.addItem(createItemSP)
                                .subscribe(this::handleResponse, this::handleError)

                            alertDialogTambah.hide()

                        }.create()

                confirmationDialog.show()
            }
        }

        alertDialogTambah.setView(dialogRootView.root)
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


        // ------------------------------ INIT STATUS PENUGASAN START ------------------------------
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
        // ------------------------------ INIT STATUS PENUGASAN END --------------------------------


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
                    dialogRootView.etKodePekerjaan.error = null
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
                    dialogRootView.etKategori.error = null
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
////                activity.disposable = masterViewModel.getItemCodeLlist("all", s.toString())
////                    .debounce(5, TimeUnit.SECONDS).subscribe(
////                        this@TambahItemDialog::handleResponse,
////                        this@TambahItemDialog::handleError
////                    )
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (dialogRootView.etJenisBarang.isFocused) {
////                    jenisBarangAdapter.filter.filter(s)
//                    dialogRootView.rvJenisBarang.visibility = View.VISIBLE
//                }
//        })

        dialogRootView.etJenisBarang.textChangeEvents().skipInitialValue()
            .debounce(2, TimeUnit.SECONDS).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                activity.disposable = masterViewModel.getItemCodeLlist("all", it.text.toString())
                    .subscribe(
                        this@TambahItemDialog::handleResponse,
                        this@TambahItemDialog::handleError
                    )
            }

        dialogRootView.etSatuan.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (dialogRootView.etSatuan.isFocused) {
                    dialogRootView.etSatuan.error = null
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
//            if (dialogRootView.rvStatusPenugasan.visibility == View.GONE) {
//                dialogRootView.rvStatusPenugasan.visibility = View.VISIBLE
//            }
//        }
    }

    fun show() {
        dialogRootView.root.clearFocus()
        alertDialogTambah.show()
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

            is CreateItemSPResponse -> {
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