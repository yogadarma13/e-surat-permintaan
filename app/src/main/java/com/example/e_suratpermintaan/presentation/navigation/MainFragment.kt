package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMyData
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    lateinit var spDisposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_loginFragment))

        spDisposable = suratPermintaanViewModel
            .readMyData("21")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError)
    }

    override fun onDetach() {
        if (!spDisposable.isDisposed) {
            spDisposable.dispose()
        }

        super.onDetach()
    }

    private fun handleResponse(response: MyDataResponse) {
        val mySuratPermintaanList: List<DataMyData?>? = response.data

        mySuratPermintaanList?.forEach {
            // textView.text = textView.text.toString() + it?.namaProyek + "\n"
        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
    }

}
