package com.example.e_suratpermintaan.framework.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File

object Directory {

    fun checkDirectoryAndFileExists(context: Context, fileName: String): Boolean {
        val outputDirectory: File
        val path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/E-SuratPermintaan"

        outputDirectory = if (isSDCardPresent()) {
            File(path)
        } else {
            Toast.makeText(context, "Memori tidak ditemukan", Toast.LENGTH_LONG).show()
            return false
        }
        return if (!outputDirectory.exists()) {
            outputDirectory.mkdir()
            true
        } else {
            val f = File("$path/$fileName")
            if (f.exists()) {
                Toast.makeText(context, "File sudah ada\nLokasi file di Penyimpanan/Download/E-SuratPermintaan/$fileName", Toast.LENGTH_LONG)
                    .show()
                false
            } else {
                true
            }
        }
    }

    private fun isSDCardPresent(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}