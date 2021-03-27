package com.example.e_suratpermintaan.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.e_suratpermintaan.databinding.DialogAddImageOptionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddImageOptionDialog : DialogFragment() {
    private var _binding: DialogAddImageOptionBinding? = null
    private val binding get() = _binding!!

    var onCameraButtonClick: (() -> Unit)? = null
    var onGalleryButtonClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddImageOptionBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())

        binding.btnCamera.setOnClickListener {
            onCameraButtonClick?.invoke()
            dialog?.dismiss()
        }

        binding.btnGallery.setOnClickListener {
            onGalleryButtonClick?.invoke()
            dialog?.dismiss()
        }

        dialogBuilder.setView(binding.root)

        return dialogBuilder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}