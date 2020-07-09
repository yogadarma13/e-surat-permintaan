package com.example.e_suratpermintaan.presentation.dialog

import android.app.Dialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.UpdateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.RoleConstants
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPA
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPB
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPS
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseFilterableAdapter
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.PersyaratanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.CCViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.JenisBarangViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.StatusPenugasanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.UomViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_edit_item.view.*
import kotlinx.android.synthetic.main.dialog_edit_item_form_keterangan.view.*
import kotlinx.android.synthetic.main.dialog_edit_item_form_spa.view.*
import kotlinx.android.synthetic.main.dialog_edit_item_form_spb.view.*
import kotlinx.android.synthetic.main.dialog_edit_item_form_sps.view.*
import java.text.SimpleDateFormat
import java.util.*

class EditItemDialog(
    private val activity: EditSuratPermintaanActivity,
    private val sharedMasterData: SharedMasterData,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel
) {

    companion object {
        const val VISIBLE = 1
    }

    private var alertDialogEdit: AlertDialog
    private var datePicker: MaterialDatePicker<Long>

    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder>
    private lateinit var statusPenugasanAdapter: BaseAdapter<StatusPenugasanViewHolder>
    private lateinit var persyaratanAdapter: BaseAdapter<PersyaratanViewHolder>

    private var dialogRootView: View = View.inflate(activity, R.layout.dialog_edit_item, null)

    private lateinit var userRoleId: String

    private lateinit var selectedItemId: String

    init {
        // https://stackoverflow.com/questions/445731372/disabling-android-o-auto-fill-service-for-an-application/45733114
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogRootView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            dialogRootView.etWaktuPemakaian.showSoftInputOnFocus = false
//            dialogRootView.etWaktuPelaksanaan.showSoftInputOnFocus = false
//        }

        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView)

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
        dialogRootView.etWaktuPemakaian.setText(dateString)
    }

    private val waktuPelaksanaanDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.etWaktuPelaksanaan.setText(dateString)
    }

    private val targetDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.etTarget.setText(dateString)
    }

    fun initDialogViewEdit(dataProfile: DataProfile, dataDetailSP: DataDetailSP) {
        dialogRootView.etVolume.setText("0.0")
        setupAlertDialog(dataProfile, dataDetailSP)
        userRoleId = dataProfile.roleId.toString()
    }

    private fun setupDatePickerListener() {
        dialogRootView.etWaktuPemakaian.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity.closeKeyboard(dialogRootView.etWaktuPemakaian)
                datePicker.addOnPositiveButtonClickListener(waktuPemakaianDateSubmitListener)
                datePicker.show(activity.supportFragmentManager, datePicker.toString())
            }
        }

        dialogRootView.pickWaktuPemakaian.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener(waktuPemakaianDateSubmitListener)
            datePicker.show(activity.supportFragmentManager, datePicker.toString())
        }


        dialogRootView.etWaktuPelaksanaan.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity.closeKeyboard(dialogRootView.etWaktuPelaksanaan)
                datePicker.addOnPositiveButtonClickListener(waktuPelaksanaanDateSubmitListener)
                datePicker.show(activity.supportFragmentManager, datePicker.toString())
            }
        }

        dialogRootView.pickWaktuPelaksanaan.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener(waktuPelaksanaanDateSubmitListener)
            datePicker.show(activity.supportFragmentManager, datePicker.toString())
        }

        dialogRootView.etTarget.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                activity.closeKeyboard(dialogRootView.etTarget)
                datePicker.addOnPositiveButtonClickListener(targetDateSubmitListener)
                datePicker.show(activity.supportFragmentManager, datePicker.toString())
            }
        }

        dialogRootView.pickTarget.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener(targetDateSubmitListener)
            datePicker.show(activity.supportFragmentManager, datePicker.toString())
        }
    }

    private fun populateAdapters() {
        sharedMasterData.getCostCodeList().observe(activity, Observer {
            it?.forEach { item ->
                ccAdapter.itemList.add(item as DataMasterCC)
                jenisBarangAdapter.itemList.add(item)
                Log.d("MYAPP", item.toString())
            }
            ccAdapter.oldItemList = ccAdapter.itemList
            ccAdapter.notifyDataSetChanged()

            jenisBarangAdapter.oldItemList = jenisBarangAdapter.itemList
            jenisBarangAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getUomList().observe(activity, Observer {
            it?.forEach { item ->
                uomAdapter.itemList.add(item as DataMasterUOM)
            }
            uomAdapter.oldItemList = uomAdapter.itemList
            uomAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getStatusPenugasanList().observe(activity, Observer {
            it?.forEach { item ->
                statusPenugasanAdapter.itemList.add(item as DataMasterOption)
            }
            statusPenugasanAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getPersyaratanList().observe(activity, Observer {
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
                dialogRootView.formSPA.visibility = View.VISIBLE
            }
            JENIS_PERMINTAAN_SPB -> {
                dialogRootView.formSPB.visibility = View.VISIBLE
            }
            JENIS_PERMINTAAN_SPS -> {
                dialogRootView.formSPS.visibility = View.VISIBLE
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
            dialogRootView.tilKapasitas.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputMerk == VISIBLE) {
            dialogRootView.tilMerk.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputWaktuPemakaian == VISIBLE) {
            dialogRootView.tilWaktuPemakaian.visibility = View.VISIBLE
            dialogRootView.pickWaktuPemakaian.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputFungsi == VISIBLE) {
            dialogRootView.tilFungsi.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputTarget == VISIBLE) {
            dialogRootView.tilTarget.visibility = View.VISIBLE
            dialogRootView.pickTarget.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputWaktuPelaksanaan == VISIBLE) {
            dialogRootView.tilWaktuPelaksanaan.visibility = View.VISIBLE
            dialogRootView.pickWaktuPelaksanaan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputPersyaratan == VISIBLE) {
            dialogRootView.tvPersyaratan.visibility = View.VISIBLE
            dialogRootView.rvPersyaratan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputKeterangan == VISIBLE) {
            dialogRootView.formKeterangan.visibility = View.VISIBLE
        }

        if (dataDetailSP.inputPenugasan == VISIBLE) {
            dialogRootView.tilStatusPenugasan.visibility = View.VISIBLE
        }

        dialogRootView.btnEdit.setOnClickListener {
            onSubmited(kodeSp, dataProfile)
        }

        alertDialogEdit.setView(dialogRootView)
    }

    private fun onSubmited(
        kodeSp: String,
        dataProfile: DataProfile
    ) {
        // Untuk menghilangkan focus yang ada di input field
        // dialogRootView.clearFocus()
        dialogRootView.performClick()

        val kodePekerjaan = dialogRootView.etKodePekerjaan.text.toString()
        val jenisBarang = dialogRootView.etJenisBarang.text.toString()
        val volume = dialogRootView.etVolume.text.toString()
        val satuan = dialogRootView.etSatuan.text.toString()
        val waktuPemakaian = dialogRootView.etWaktuPemakaian.text.toString()

        val kapasitas = dialogRootView.formSPA.etKapasitas.text.toString()
        val merk = dialogRootView.formSPA.etMerk.text.toString()
        val fungsi = dialogRootView.formSPB.etFungsi.text.toString()
        val target = dialogRootView.formSPB.etTarget.text.toString()
        val waktuPelaksanaan = dialogRootView.formSPS.etWaktuPelaksanaan.text.toString()

        val statusPenugasanOption = dialogRootView.etStatusPenugasan.text.toString()
        val statusPenugasanValue = (statusPenugasanAdapter.itemList.find
        { (it as DataMasterOption).option == statusPenugasanOption } as DataMasterOption?)?.value.toString()

        val persyaratanList: ArrayList<String> = arrayListOf()
        persyaratanAdapter.itemList.forEach {
            val data = it as DataMasterPersyaratan
            if (data.isChecked) {
                persyaratanList.add(data.id.toString())
            }
        }

        val keterangan = dialogRootView.formKeterangan.etKeterangan.text.toString()

        val confirmationDialog = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
            .setTitle("Edit Item")
            .setMessage("Apakah Anda yakin ingin mengupdate item?")
            .setPositiveButton("Ya") { _, _ ->
                val updateItemSP = UpdateItemSP(
                    kodeSp,
                    kodePekerjaan,
                    jenisBarang,
                    satuan,
                    volume,
                    fungsi,
                    target,
                    keterangan,
                    kapasitas,
                    merk,
                    waktuPemakaian,
                    waktuPelaksanaan,
                    persyaratanList,
                    statusPenugasanValue.toString(),
                    dataProfile.id!!,
                    selectedItemId
                )
                activity.disposable = itemSuratPermintaanViewModel.editItem(updateItemSP)
                    .subscribe(this::handleResponse, this::handleError)

                alertDialogEdit.hide()

            }.create()

        confirmationDialog.show()
    }

    private fun preventKeyboardFromPushingViews(dialog: Dialog?) {
        val window: Window? = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun hideAllRecyclerViews() {
        dialogRootView.rvKodePekerjaan.visibility = View.GONE
        dialogRootView.rvJenisBarang.visibility = View.GONE
        dialogRootView.rvSatuan.visibility = View.GONE
        dialogRootView.rvStatusPenugasan.visibility = View.GONE
    }

    private fun setupRecyclerViews() {
        // ------------------------------ INIT CC START ---------------------------------------------
        ccAdapter = BaseFilterableAdapter(R.layout.item_simple_row, CCViewHolder::class.java)
        ccAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etKodePekerjaan.setText((item as DataMasterCC).kodeCostcontrol)

            if (userRoleId != RoleConstants.CC) {
                dialogRootView.etJenisBarang.setText(item.deskripsi)
                dialogRootView.etSatuan.setText(item.uom)
            }

            // Perlu ini karna pas setText recyclerview suggestion nya si kodePekerjaan, jenisBarang,
            // & satuan muncul. Jadi harusnya di hide.
            hideAllRecyclerViews()

            activity.closeKeyboard(dialogRootView.etKodePekerjaan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvKodePekerjaan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvKodePekerjaan.adapter = ccAdapter
        // ------------------------------ INIT CC END ---------------------------------------------


        // ------------------------------ INIT JENIS START ----------------------------------------
        jenisBarangAdapter =
            BaseFilterableAdapter(R.layout.item_simple_row, JenisBarangViewHolder::class.java)
        jenisBarangAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etJenisBarang.setText((item as DataMasterCC).deskripsi)
            dialogRootView.rvJenisBarang.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etJenisBarang)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvJenisBarang.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvJenisBarang.adapter = jenisBarangAdapter
        // ------------------------------ INIT JENIS END ------------------------------------------


        // ------------------------------ INIT UOM START ------------------------------------------
        uomAdapter = BaseFilterableAdapter(R.layout.item_simple_row, UomViewHolder::class.java)
        uomAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etSatuan.setText((item as DataMasterUOM).nama)
            dialogRootView.rvSatuan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etSatuan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvSatuan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvSatuan.adapter = uomAdapter
        // ------------------------------ INIT UOM END --------------------------------------------


        // -------------------------- INIT STATUS PENUGASAN START ---------------------------------
        statusPenugasanAdapter =
            BaseAdapter(R.layout.item_simple_row, StatusPenugasanViewHolder::class.java)
        statusPenugasanAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etStatusPenugasan.setText((item as DataMasterOption).option)
            dialogRootView.rvStatusPenugasan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etStatusPenugasan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvStatusPenugasan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvStatusPenugasan.adapter = statusPenugasanAdapter
        // --------------------------- INIT STATUS PENUGASAN END ----------------------------------


        // ----------------------------- INIT PERSYARATAN START -----------------------------------
        persyaratanAdapter =
            BaseAdapter(R.layout.item_simple_checkbox, PersyaratanViewHolder::class.java)
        dialogRootView.formSPS.rvPersyaratan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.formSPS.rvPersyaratan.adapter = persyaratanAdapter
        // ----------------------------- INIT PERSYARATAN END -----------------------------------
    }

    private fun setupTextChangeListener() {
        dialogRootView.etKodePekerjaan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (dialogRootView.etKodePekerjaan.isFocused) {
                    ccAdapter.filter.filter(s)
                    dialogRootView.rvKodePekerjaan.visibility = View.VISIBLE
                }
            }
        })

        dialogRootView.etJenisBarang.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                jenisBarangAdapter.filter.filter(s)
                dialogRootView.rvJenisBarang.visibility = View.VISIBLE
            }
        })

        dialogRootView.etSatuan.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                uomAdapter.filter.filter(s)
                dialogRootView.rvSatuan.visibility = View.VISIBLE
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

        dialogRootView.etStatusPenugasan.setOnClickListener {
            dialogRootView.rvStatusPenugasan.visibility = View.VISIBLE
        }
    }

    fun show(itemsDetailSP: ItemsDetailSP) {
        dialogRootView.clearFocus()

        selectedItemId = itemsDetailSP.id.toString()

        dialogRootView.etKodePekerjaan.setText(itemsDetailSP.kodePekerjaan)
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
        dialogRootView.etKeterangan.setText("")

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
        activity.handleResponse(response)
    }

    private fun handleError(error: Throwable) {
        activity.handleError(error)
    }

}