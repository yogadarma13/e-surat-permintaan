package com.example.e_suratpermintaan.presentation.dialog

import android.app.Dialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.e_suratpermintaan.core.domain.entities.requests.PenugasanItemSP
import com.e_suratpermintaan.core.domain.entities.responses.DataMaster
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.DialogPenugasanItemBinding
import com.example.e_suratpermintaan.presentation.activity.EditSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewmodel.ItemSuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PenugasanItemDialog(
    private val activity: EditSuratPermintaanActivity,
    private val sharedMasterData: SharedMasterData,
    private val itemSuratPermintaanViewModel: ItemSuratPermintaanViewModel
) {

    private var alertDialogEdit: AlertDialog
    private lateinit var itemDetailSP: ItemsDetailSP
    private val penugasanOptionList: ArrayList<DataMaster> = arrayListOf()
    private var penugasanOptionAdapter: ArrayAdapter<DataMaster>

    private var dialogRootView: DialogPenugasanItemBinding =
        DialogPenugasanItemBinding.inflate(LayoutInflater.from(activity), null, false)
//        View.inflate(activity, R.layout.dialog_penugasan_item, null)

    init {
        // https://stackoverflow.com/questions/45731372/disabling-android-o-auto-fill-service-for-an-application/45733114
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

        penugasanOptionAdapter =
            ArrayAdapter(activity, R.layout.material_spinner_item, penugasanOptionList)

        dialogRootView.spinnerKepada.setAdapter(penugasanOptionAdapter)

        populateAdapters()
    }

    fun initDialogViewPenugasan(dataProfile: DataProfile) {
        setupAlertDialog(dataProfile)
    }

    private fun populateAdapters() {
        sharedMasterData.getPenugasanList().observe(activity, {
            it?.forEach { item ->
                penugasanOptionList.add(item as DataMaster)
            }
            penugasanOptionAdapter.notifyDataSetChanged()
        })
    }

    private fun setupAlertDialog(dataProfile: DataProfile) {
        dialogRootView.btnDitugaskanKepada.setOnClickListener {
            // Untuk menghilangkan focus yang ada di input field
            dialogRootView.root.clearFocus()

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

        alertDialogEdit.setView(dialogRootView.root)
    }

    private fun preventKeyboardFromPushingViews(dialog: Dialog?) {
        val window: Window? = dialog?.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun show(itemsDetailSP: ItemsDetailSP) {
        itemDetailSP = itemsDetailSP

        val selectedObj = penugasanOptionList.find { it.value.toString() == itemsDetailSP.kepada }
        val selectedOption = selectedObj?.option
        dialogRootView.spinnerKepada.setText(selectedOption, false)
        alertDialogEdit.show()
    }

    private fun handleResponse(response: Any) {
        activity.handleResponse(response)
    }

    private fun handleError(error: Throwable) {
        activity.handleError(error)
    }

}