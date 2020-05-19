package com.example.e_suratpermintaan.framework.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Environment
import android.os.PowerManager
import android.widget.Toast
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadTask(context: Context, fileName: String) : AsyncTask<String, Int, String>() {

    private val context = context
    private lateinit var mWakeLock: PowerManager.WakeLock
    private val mProgressDialog = ProgressDialog(context)
    private val fileName = fileName

    override fun doInBackground(vararg params: String?): String? {
        var url = params[0] as String

        var input: InputStream? = null
        var output: OutputStream? = null
        var connection: HttpURLConnection? = null

        try {
            val url = URL(url)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode !== HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                    .toString() + " " + connection.getResponseMessage()
            }
            val fileLength: Int = connection.getContentLength()
            input = connection.getInputStream()
            output = FileOutputStream(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/E-SuratPermintaan/" + fileName
            )
            val data = ByteArray(4096)
            var total: Long = 0
            var count: Int = 0
            while (input.read(data).also { count = it } != -1) {
                if (isCancelled) {
                    input.close()
                    return null
                }
                total += count.toLong()
                if (fileLength > 0) publishProgress((total * 100 / fileLength).toInt())
                output.write(data, 0, count)
            }
        } catch (e: Exception) {
            return e.toString()
        } finally {
            try {
                output?.close()
                input?.close()
            } catch (ignored: IOException) {
            }
            connection?.disconnect()
        }
        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
        mProgressDialog.setMessage("Downloading...")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batal") { _, _ ->
            this.cancel(true)
        }

        val pm: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            javaClass.name
        )
        mWakeLock.acquire()
        mProgressDialog.show()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        mProgressDialog.isIndeterminate = false
        mProgressDialog.max = 100
        mProgressDialog.progress = values[0] as Int
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        mWakeLock?.release()

        mProgressDialog.dismiss()
        if (result != null) Toast.makeText(context, "Download error: $result", Toast.LENGTH_LONG)
            .show()
        else {
            Toast.makeText(
                context,
                "Download berhasil\nLokasi file di Penyimpanan/Download/E-SuratPermintaan/$fileName",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}