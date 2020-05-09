package com.example.e_suratpermintaan.presentation.dialog

import android.app.Dialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.responses.DataMasterOption
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_penugasan_item.view.*

class PenugasanItemDialog(
    private val activity: EditSuratPermintaanActivity,
    private val sharedViewModel: SharedViewModel,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel
) {

    private var alertDialogEdit: AlertDialog
    private lateinit var itemDetailSP: ItemsDetailSP
    private val penugasanOptionList: ArrayList<DataMasterOption> = arrayListOf()
    private var penugasanOptionAdapter: ArrayAdapter<DataMasterOption>

    private var dialogRootView: View = View.inflate(activity, R.layout.dialog_penugasan_item, null)

    init {
        activity.findAndSetEditTextFocusChangeListenerRecursively(dialogRootView)

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
                .setTitle("Edit Item")
        alertDialogEdit = alertDialogBuilder.create()

        // Ini dipakai biar supaya pas keyboard showup, gak ngepush view dialog
        preventKeyboardFromPushingViews(alertDialogEdit)

        penugasanOptionAdapter =
            ArrayAdapter(activity, R.layout.material_spinner_item, penugasanOptionList)

        dialogRootView.spinnerKepada.setAdapter(penugasanOptionAdapter)

        populateAdapters()
    }

    fun initDialogViewPenugasan(dataProfile: DataProfile) {
        setupAlertDialog(dataProfile)
    }

    private fun populateAdapters() {
        sharedViewModel.getPenugasanList().observe(activity, Observer {
            it?.forEach { item ->
                penugasanOptionList.add(item as DataMasterOption)
            }
            penugasanOptionAdapter.notifyDataSetChanged()
        })
    }

    private fun setupAlertDialog(dataProfile: DataProfile) {
        dialogRootView.btnDitugaskanKepada.setOnClickListener {
            // Untuk menghilangkan focus yang ada di input field
            dialogRootView.clearFocus()

            val selectedPenugasan = dialogRootView.spinnerKepada.text.toString()

            val selectedPenugasanKepadaValue =
                penugasanOptionList.find { it.option == selectedPenugasan }?.value ?: ""

            val penugasanItemSP = PenugasanItemSP(
                dataProfile.id.toString(),
                itemDetailSP.id.toString(),
                selectedPenugasanKepadaValue
            )
            activity.disposable = itemSuratPermintaanViewModel.setPenugasanItem(penugasanItemSP)
                .subscribe(this::handleResponse, this::handleError)

            alertDialogEdit.hide()

        }

        alertDialogEdit.setView(dialogRootView)
    }

    private fun preventKeyboardFromPushingViews(dialog: Dialog?) {
        val window: Window? = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun show(itemsDetailSP: ItemsDetailSP) {
        itemDetailSP = itemsDetailSP
        dialogRootView.spinnerKepada.text.clear()
        alertDialogEdit.show()
    }

    private fun handleResponse(response: Any) {
        activity.handleResponse(response)
    }

    private fun handleError(error: Throwable) {
        activity.handleError(error)
    }

}