package com.example.e_suratpermintaan.presentation.navigation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e_suratpermintaan.core.domain.entities.responses.MyDataResponse
import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataMyData
import com.evrencoskun.tableview.pagination.Pagination
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.helpers.NavOptionsHelper
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseFragment
import com.example.e_suratpermintaan.presentation.tableview.MyTableAdapter
import com.example.e_suratpermintaan.presentation.tableview.MyTableViewListener
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */

class MainFragment : BaseFragment() {

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val profilePreference: ProfilePreference by inject()
    private lateinit var mTableAdapter: MyTableAdapter
    private lateinit var mPagination: Pagination

    override fun layoutId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocus()

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
            toastNotify(profileId + "")
            disposable = suratPermintaanViewModel.readAllData(profileId)
                .subscribe(this::handleResponse, this::handleError)
        }
    }

    private fun handleResponse(response: MyDataResponse) {
        val suratPermintaanList: List<DataMyData?>? = response.data
        mTableAdapter.setDataList(suratPermintaanList)
        mPagination.goToPage(1)
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
    }

}
