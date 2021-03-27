package com.example.e_suratpermintaan.framework.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.e_suratpermintaan.R
import java.io.File
import java.io.FileOutputStream

object DownloadPath {

    fun getDownloadPath(context: Context): String? {
        val path: String?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(
                MediaStore.Downloads.RELATIVE_PATH,
                "${Environment.DIRECTORY_DOWNLOADS}${File.separator}${context.getString(R.string.app_name)}"
            )
            contentValues.put(MediaStore.Downloads.DATE_ADDED, System.currentTimeMillis() / 1000)
            contentValues.put(MediaStore.Downloads.DATE_TAKEN, System.currentTimeMillis())
            val resolver = context.contentResolver
            val uri =
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            path = uri?.path
        } else {
            path = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/E-Surat Permintaan"

        }

        return path
    }
}