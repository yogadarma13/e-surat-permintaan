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
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterUOM
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.base.BaseFilterableAdapter
import com.example.e_suratpermintaan.presentation.viewholders.CCViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.JenisBarangViewHolder
import com.example.e_suratpermintaan.presentation.viewholders.UomViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_tambah_item.view.*

class TambahItemDialog(
    private val activity: DetailSuratPermintaanActivity,
    private val sharedViewModel: SharedViewModel,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel
) {

    private lateinit var alertDialogTambah: AlertDialog
    private lateinit var ccAdapter: BaseFilterableAdapter<CCViewHolder>
    private lateinit var jenisBarangAdapter: BaseFilterableAdapter<JenisBarangViewHolder>
    private lateinit var uomAdapter: BaseFilterableAdapter<UomViewHolder>
    private lateinit var dialogRootView: View

    fun initDialogViewTambah(kodeSp: String, idUser: String) {
        dialogRootView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_tambah_item, null)

        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView)

        setupTextChangeListener()
        hideAllRecyclerViews()
        initRecyclerViews()
        populateAdapterList()
        setupAlertDialog(kodeSp, idUser)
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
    }

    private fun setupAlertDialog(kodeSp: String, idUser: String) {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                .setTitle("Tambah Item")
        alertDialogTambah = alertDialogBuilder.create()

        dialogRootView.btnTambah.setOnClickListener {
            val kodePekerjaan = dialogRootView.etKodePekerjaan.text.toString()
            val jenisBarang = dialogRootView.etJenisBarang.text.toString()
            val volume = dialogRootView.etVolume.text.toString()
            val satuan = dialogRootView.etSatuan.text.toString()
            val kapasitas = dialogRootView.etKapasistas.text.toString()
            val merk = dialogRootView.etMerk.text.toString()
            val waktuPemakaian = dialogRootView.etWaktuPemakaian.text.toString()

            alertDialogTambah.hide()
            val newAlertDialog = alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin menambah item?")
                .setPositiveButton("Ya") { _, _ ->
                    val createItemSP = CreateItemSP(
                        kodeSp,
                        dialogRootView.etKodePekerjaan.text.toString(),
                        dialogRootView.etJenisBarang.text.toString(),
                        dialogRootView.etSatuan.text.toString(),
                        "50",
                        "",
                        "",
                        "",
                        dialogRootView.etKapasistas.text.toString(),
                        "",
                        "",
                        "2020-04-20",
                        arrayListOf("1", "2"),
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
        ccAdapter = BaseFilterableAdapter(R.layout.simple_item_row, CCViewHolder::class.java)
        ccAdapter.setOnItemClickListener {
            dialogRootView.etKodePekerjaan.setText((it as DataMasterCC).kodeCostcontrol)
            dialogRootView.rvKodePekerjaan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etKodePekerjaan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvKodePekerjaan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvKodePekerjaan.adapter = ccAdapter

        jenisBarangAdapter =
            BaseFilterableAdapter(R.layout.simple_item_row, JenisBarangViewHolder::class.java)
        jenisBarangAdapter.setOnItemClickListener {
            dialogRootView.etJenisBarang.setText((it as DataMasterCC).deskripsi)
            dialogRootView.rvJenisBarang.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etJenisBarang)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvJenisBarang.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvJenisBarang.adapter = jenisBarangAdapter

        uomAdapter = BaseFilterableAdapter(R.layout.simple_item_row, UomViewHolder::class.java)
        uomAdapter.setOnItemClickListener {
            dialogRootView.etSatuan.setText((it as DataMasterUOM).nama)
            dialogRootView.rvSatuan.visibility = View.GONE
            activity.closeKeyboard(dialogRootView.etSatuan)
            dialogRootView.container.performClick()
        }
        dialogRootView.rvSatuan.layoutManager = LinearLayoutManager(activity)
        dialogRootView.rvSatuan.adapter = uomAdapter
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