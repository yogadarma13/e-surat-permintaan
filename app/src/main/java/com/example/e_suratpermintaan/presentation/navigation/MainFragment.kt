package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMasterJenisProyek
import com.evrencoskun.tableview.pagination.Pagination
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.helpers.NavOptionsHelper
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.tableview.MyTableAdapter
import com.example.e_suratpermintaan.presentation.tableview.MyTableViewListener
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.ajukan_sp_dialog.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class MainFragment : BaseFragment() {

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private lateinit var mTableAdapter: MyTableAdapter
    private lateinit var mPagination: Pagination

    private val proyekList: ArrayList<MyDataResponse> = ArrayList()
    private val jenisList: ArrayList<MyDataResponse> = ArrayList()

    override fun layoutId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocus()


        setupDialog()


        // Implementasi Data Table di android menggunakan library TableView
        mTableAdapter = MyTableAdapter(context)
        tableView.setAdapter(mTableAdapter)
        tableView.tableViewListener = MyTableViewListener(tableView)

        mPagination = Pagination(tableView)
        mPagination.itemsPerPage = 5
        mPagination.setOnTableViewPageTurnedListener { numItems, itemsStart, itemsEnd ->
            // Relayout pagination number di sini
        }

        btnPrev.setOnClickListener{
            mPagination.previousPage()
        }

        btnNext.setOnClickListener {
            mPagination.nextPage()
        }

        btnLogout.setOnClickListener {
            profilePreference.removeProfile()
            val navOptions =
                NavOptionsHelper.getInstance().addBackToSplashAnim()
                    .clearBackStack(R.id.welcomeFragment).build()
            it.findNavController()
                .navigate(R.id.action_mainFragment_to_splashScreen, null, navOptions)
        }

        val profileId = profilePreference.getProfile()?.id

        if (profileId != null) {
            val spObservable = suratPermintaanViewModel.readAllData(profileId)
            val proyekObservable = masterViewModel.getProyekList(profileId)
            val jenisObservable = masterViewModel.getJenisList(profileId)

            disposable = Observable.concat(spObservable, proyekObservable, jenisObservable)
                .subscribe(this::handleResponse, this::handleError)
        }
    }

    private fun setupDialog() {
        val dialogRootView =
            requireActivity().layoutInflater.inflate(R.layout.ajukan_sp_dialog, null)

        val dows = arrayOf("A", "B", "C")
        val adapter = ArrayAdapter(requireContext(), R.layout.material_spinner_item, dows)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.material_spinner_item, dows)

        dialogRootView.spinnerDropdown.setAdapter(adapter)
        dialogRootView.spinnerDropdown2.setAdapter(adapter2)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Judul")
            .setView(dialogRootView)
            .create()
            .show()
    }

    private fun handleResponse(response: Any) {
        if (response is MyDataResponse){
            //asdasd
        } else if (response is DataMasterJenisProyek){
            //asdasd
        }
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
    }

}
