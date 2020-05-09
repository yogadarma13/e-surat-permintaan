package com.example.e_suratpermintaan.external.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.*

class Signature {

    fun saveSignature(context: Context, ttdBitmap: Bitmap): File {
        var fileTtd: File = File(context.cacheDir, "ttd")

        fileTtd.createNewFile()

        val bos = ByteArrayOutputStream()
        ttdBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)

        val bitmapdata = bos.toByteArray()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(fileTtd)
        }
        catch(e: FileNotFoundException) {
            e.printStackTrace()
        }

        try {
            fos?.write(bitmapdata)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return fileTtd
    }
}