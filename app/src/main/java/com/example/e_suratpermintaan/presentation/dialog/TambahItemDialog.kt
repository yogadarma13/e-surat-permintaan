package com.example.e_suratpermintaan.presentation.dialog

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.CreateItemSP
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterCC
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterPersyaratan
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterUOM
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPA
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPB
import com.example.e_suratpermintaan.external.constants.SuratPermintaanConstants.Companion.JENIS_PERMINTAAN_SPS
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.base.BaseFilterableAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.CCViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.JenisBarangViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.PersyaratanViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.usingbasefilterableadapter.UomViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_tambah_item.view.*
import kotlinx.android.synthetic.main.dialog_tambah_item_form_keterangan.view.*
import kotlinx.android.synthetic.main.dialog_tambah_item_form_spa.view.*
import kotlinx.android.synthetic.main.dialog_tambah_item_form_spb.view.*
import kotlinx.android.synthetic.main.dialog_tambah_item_form_sps.view.*

class TambahItemDialog(
    private val activity: DetailSuratPermintaanActivity,
    private val sharedViewModel: SharedViewModel,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel
) {

    private lateinit var alertDialogTambah: AlertDialog
    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder>
    private lateinit var persyaratanAdapter: BaseAdapter<PersyaratanViewHolder>
    private lateinit var dialogRootView: View

    fun initDialogViewTambah(kodeSp: String, idUser: String, jenisPermintaan: String) {
        dialogRootView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_tambah_item, null)

        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView)

        setupAlertDialog(kodeSp, idUser, jenisPermintaan)
        setupTextChangeListener()
        hideAllRecyclerViews()
        initRecyclerViews()
        populateAdapterList()
    }

    private fun populateAdapterList() {
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

    private fun setupAlertDialog(kodeSp: String, idUser: String, jenisPermintaan: String) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                .setTitle("Tambah Item")
        alertDialogTambah = alertDialogBuilder.create()

        when (jenisPermintaan){
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

        dialogRootView.btnTambah.setOnClickListener {
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

            val persyaratanList : ArrayList<String> = arrayListOf()
            persyaratanAdapter.itemList.forEach {
                val data = it as DataMasterPersyaratan
                if (data.status.equals("checked")){
                    persyaratanList.add(data.id.toString())
                }
            }

            val keterangan = dialogRootView.formKeterangan.etKeterangan.text.toString()

                alertDialogTambah.hide()
            val newAlertDialog = alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin menambah item?")
                .setPositiveButton("Ya") { _, _ ->
                    val createItemSP = CreateItemSP(
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
                        idUser
                    )
                    activity.disposable = itemSuratPermintaanViewModel.addItem(createItemSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialogTambah.hide()

                }.create()

            newAlertDialog.show()
        }

        alertDialogTambah.setView(dialogRootView)
    }

    private fun hideAllRecyclerViews() {
        dialogRootView.rvKodePekerjaan.visibility = View.GONE
        dialogRootView.rvJenisBarang.visibility = View.GONE
        dialogRootView.rvSatuan.visibility = View.GONE
    }

    private fun initRecyclerViews() {
        ccAdapter = BaseFilterableAdapter(R.layout.item_simple_row, CCViewHolder::class.java)
        ccAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etKodePekerjaan.setText((item as DataMasterCC).kodeCostcontrol)
            dialogRootView.etJenisBarang.setText(item.deskripsi)
            dialogRootView.etSatuan.setText(item.uom)

            hideAllRecyclerViews()

            activity.closeKeyboard(dialogRootView.etKodePekerjaan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvKodePekerjaan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvKodePekerjaan.adapter = ccAdapter

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

        uomAdapter = BaseFilterableAdapter(R.layout.item_simple_row, UomViewHolder::class.java)
        uomAdapter.setOnItemClickListener { item, _ ->
            dialogRootView.etSatuan.setText((item as DataMasterUOM).nama)
            dialogRootView.rvSatuan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etSatuan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvSatuan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvSatuan.adapter = uomAdapter

        persyaratanAdapter =
            BaseAdapter(R.layout.item_simple_checkbox, PersyaratanViewHolder::class.java)
        dialogRootView.formSPS.rvPersyaratan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.formSPS.rvPersyaratan.adapter = persyaratanAdapter
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

    fun show() {
        alertDialogTambah.show()
    }

    private fun handleResponse(response: Any) {
        activity.handleResponse(response)
    }

    private fun handleError(error: Throwable) {
        activity.handleError(error)
    }

}