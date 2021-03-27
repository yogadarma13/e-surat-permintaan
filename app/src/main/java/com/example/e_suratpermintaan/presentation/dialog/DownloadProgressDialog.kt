package com.example.e_suratpermintaan.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.DialogDownloadProgressBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DownloadProgressDialog : DialogFragment() {
    private var _binding: DialogDownloadProgressBinding? = null
    private val binding get() = _binding!!
    private var progress = 0

    fun setProgress(progress: Int) {
        this.progress = progress

        binding.progressbarDownload.progress = progress
        binding.tvProgressDownload.text = this.getString(R.string.percentage_download, progress)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDownloadProgressBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = MaterialAlertDialogBuilder(context)

        dialogBuilder.setView(binding.root)

        isCancelable = false

        return dialogBuilder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}