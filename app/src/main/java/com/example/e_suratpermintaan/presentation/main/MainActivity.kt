package com.example.e_suratpermintaan.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.e_suratpermintaan.core.data.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.data.entities.responses.data_response.DataMyData
import com.example.e_suratpermintaan.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.ext.android.inject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val suratPermintaanViewModel: SuratPermintaanViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        suratPermintaanViewModel
            .readMyData("21")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError)
    }

    private fun handleResponse(response: MyDataResponse) {
        val mySuratPermintaanList: List<DataMyData?>? = response.data

        mySuratPermintaanList?.forEach {
            textView.text = textView.text.toString() + it?.namaProyek + "\n"
        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(applicationContext, error.message.toString(), Toast.LENGTH_LONG).show()
    }

}
