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
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPA
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPB
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPS
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseFilterableAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.PersyaratanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.CCViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.JenisBarangViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.UomViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
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
    private val activity: DetailSuratPermintaanActivity,
    private val sharedViewModel: SharedViewModel,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel
) {

    private lateinit var alertDialogEdit: AlertDialog
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder>
    private lateinit var persyaratanAdapter: BaseAdapter<PersyaratanViewHolder>

    private var dialogRootView: View = View.inflate(activity, R.layout.dialog_edit_item, null)

    private lateinit var selectedItemId: String

    init {
        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogRootView.etWaktuPemakaian.showSoftInputOnFocus = false
            dialogRootView.etWaktuPelaksanaan.showSoftInputOnFocus = false
        }

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                .setTitle("Edit Item")
        alertDialogEdit = alertDialogBuilder.create()

        // Ini dipakai biar supaya pas keyboard showup, gak ngepush view dialog
        preventKeyboardFromPushingViews(alertDialogEdit)

        val builder = MaterialDatePicker.Builder.datePicker()
        datePicker = builder.build()

        setupRecyclerViews()
        populateAdapters()
        setupDatePickerListener()
        setupTextChangeListener()
    }

    private val waktuPemakaianDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.let {
            it.etWaktuPemakaian.setText(dateString)
        }
    }

    private val waktuPelaksanaanDateSubmitListener: ((Long) -> Unit) = { selectedDate ->
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date(selectedDate)
        val dateString = simpleFormat.format(date)
        dialogRootView.let {
            it.etWaktuPelaksanaan.setText(dateString)
        }
    }

    fun initDialogViewEdit(dataProfile: DataProfile, dataDetailSP: DataDetailSP) {
        dialogRootView.etVolume.setText("0.0")
        setupAlertDialog(dataProfile, dataDetailSP)
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
    }

    private fun populateAdapters() {
        sharedViewModel.getCostCodeList().observe(activity, Observer {
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

        sharedViewModel.getUomList().observe(activity, Observer {
            it?.forEach { item ->
                uomAdapter.itemList.add(item as DataMasterUOM)
            }
            uomAdapter.oldItemList = uomAdapter.itemList
            uomAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getPersyaratanList().observe(activity, Observer {
            it?.forEach { item ->
                persyaratanAdapter.itemList.add(item as DataMasterPersyaratan)
            }
            persyaratanAdapter.notifyDataSetChanged()
        })
    }

    private fun setupAlertDialog(dataProfile: DataProfile, dataDetailSP: DataDetailSP) {
        val idSp = dataDetailSP.id
        val jenisPermintaan = dataDetailSP.jenis.toString()
        val kodeSp = dataDetailSP.kode.toString()

        if (dataProfile.roleId!!.toInt() != 1) {
            dialogRootView.formKeterangan.visibility = View.VISIBLE
        }

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

        dialogRootView.btnEdit.setOnClickListener {
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

            val persyaratanList: ArrayList<String> = arrayListOf()
            persyaratanAdapter.itemList.forEach {
                val data = it as DataMasterPersyaratan
                if (data.status.equals("checked")) {
                    persyaratanList.add(data.id.toString())
                }
            }

            persyaratanList.forEach {
                activity.toastNotify(it)
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
                        dataProfile.id!!,
                        selectedItemId
                    )
                    activity.disposable = itemSuratPermintaanViewModel.editItem(updateItemSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialogEdit.hide()

                }.create()

            confirmationDialog.show()
        }

        alertDialogEdit.setView(dialogRootView)
    }

    private fun preventKeyboardFromPushingViews(dialog: Dialog?) {
        val window: Window? = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun hideAllRecyclerViews() {
        dialogRootView.rvKodePekerjaan.visibility = View.GONE
        dialogRootView.rvJenisBarang.visibility = View.GONE
        dialogRootView.rvSatuan.visibility = View.GONE
    }

    private fun setupRecyclerViews() {
        // ------------------------------ INIT CC START ---------------------------------------------
        ccAdapter = BaseFilterableAdapter(R.layout.item_simple_row, CCViewHolder::class.java)
        ccAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etKodePekerjaan.setText((item as DataMasterCC).kodeCostcontrol)
            dialogRootView.etJenisBarang.setText(item.deskripsi)
            dialogRootView.etSatuan.setText(item.uom)

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

    fun show(itemsDetailSP: ItemsDetailSP) {
        selectedItemId = itemsDetailSP.id.toString()

        dialogRootView.etKodePekerjaan.setText(itemsDetailSP.kodePekerjaan)
        dialogRootView.etJenisBarang.setText(itemsDetailSP.idBarang)
        dialogRootView.etVolume.setText(itemsDetailSP.qty)
        dialogRootView.etSatuan.setText(itemsDetailSP.idSatuan)
        dialogRootView.etWaktuPemakaian.setText(itemsDetailSP.waktuPemakaian)

        dialogRootView.formSPA.etKapasitas.setText(itemsDetailSP.kapasitas)
        dialogRootView.formSPA.etMerk.setText(itemsDetailSP.merk)
        dialogRootView.formSPB.etFungsi.setText(itemsDetailSP.fungsi)
        dialogRootView.formSPB.etTarget.setText(itemsDetailSP.target)
        dialogRootView.formSPS.etWaktuPelaksanaan.setText(itemsDetailSP.waktuPelaksanaan)

        alertDialogEdit.show()
        hideAllRecyclerViews()

        persyaratanAdapter.itemList.forEach { item ->
            val data = item as DataMasterPersyaratan
            data.status = "unchecked"

            itemsDetailSP.persyaratan?.forEach {
                if (it?.persyaratan.equals(data.id)){
                    data.status = "checked"
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