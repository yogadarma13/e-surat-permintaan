package com.example.e_suratpermintaan.framework.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object DownloadManager {

    private const val BUFFER_SIZE: Long = 2048
    private const val FLUSH_SIZE: Long = 1048 * 1048
    private const val UPDATE_RATE: Long = 1000

    private var mExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var mHandler: Handler = Handler(Looper.getMainLooper())

    fun download(url: String, path: String, callback: Callback) {
        mExecutorService.execute { downloadFile(url, path, callback) }
    }

    private fun downloadFile(url: String, path: String, callback: Callback) {
        try {
            val downloadDir = File(path)
            downloadDir.mkdirs()

            val filenameExtension = url.substring(url.lastIndexOf('/') + 1)
            val initialNumber = System.currentTimeMillis().toString().takeLast(3)
            val filename = "${initialNumber}_${filenameExtension}"

            val file = File(downloadDir, filename)

            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            val response: Response = client.newCall(request).execute()

            val sink = file.sink().buffer()

            val totalLength = response.body?.contentLength()
            notifyStarted(callback, totalLength)

            var downloadedLength: Long = 0
            var bufferSize: Long = 0
            var lastUpdate: Long = 0

            while (true) {
                val length = response.body?.source()?.read(sink.buffer, BUFFER_SIZE)

                if (length != null) {
                    if (length < 0) break

                    downloadedLength += length
                    bufferSize += length

                    if (bufferSize >= FLUSH_SIZE) {
                        sink.flush()
                        bufferSize = 0
                    }

                    if (System.currentTimeMillis() > (lastUpdate + UPDATE_RATE)) {
                        notifyProgress(callback, totalLength, downloadedLength)
                        lastUpdate = System.currentTimeMillis()
                    }
                }
            }
            sink.close()

            notifyComplete(callback, file)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("ERRRORRRR", e.message.toString())
            notifyError(callback, e)
        }
    }

    private fun notifyStarted(callback: Callback, totalLength: Long?) {
        mHandler.post { callback.onDownloadStarted(totalLength) }
    }

    private fun notifyProgress(callback: Callback, totalLength: Long?, downloadedLength: Long) {
        mHandler.post { callback.onDownloadProgress(totalLength, downloadedLength) }
    }

    private fun notifyComplete(callback: Callback, file: File) {
        mHandler.post { callback.onDownloadComplete(file) }
    }

    private fun notifyError(callback: Callback, e: Exception) {
        mHandler.post { callback.onDownloadError(e) }
    }

    interface Callback {
        fun onDownloadStarted(totalLength: Long?)

        fun onDownloadProgress(totalLength: Long?, downloadedLength: Long)

        fun onDownloadComplete(file: File)

        fun onDownloadError(e: Exception)
    }
}