package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity.Companion.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity.Companion.STATUS_SP_DELETED
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.MyDataViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.NotifikasiViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SharedViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnAjukan
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_main.swipeRefreshLayout
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.*
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.spinnerJenis
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.spinnerProyek
import kotlinx.android.synthetic.main.dialog_filter_sp.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    companion object {
        const val LAUNCH_DETAIL_ACTIVITY: Int = 111
    }

    private var selectedJenisDataFilterValue: String = ""
    private var selectedStatusFilterValue: String = ""
    private var selectedJenisPermintaanFilterValue: String = ""
    private var selectedIdProyekFilterValue: String = ""
    private var profileId: String? = null
    private lateinit var idUser: String

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by inject()
    private val profilePreference: ProfilePreference by inject()

    private lateinit var alertDialogTambahSP: AlertDialog
    private lateinit var alertDialogFilterSP: AlertDialog

    private val proyekList: ArrayList<DataMasterProyek> = arrayListOf()
    private val jenisList: ArrayList<DataMasterJenis> = arrayListOf()

    private val proyekOptionList: ArrayList<DataMasterOption> = arrayListOf()
    private val jenisPermintaanOptionList: ArrayList<DataMasterOption> = arrayListOf()
    private val statusOptionList: ArrayList<DataMasterOption> = arrayListOf()
    private val jenisDataOptionList: ArrayList<DataMasterOption> =
        arrayListOf()

    private lateinit var jenisAdapter: ArrayAdapter<DataMasterJenis>
    private lateinit var proyekAdapter: ArrayAdapter<DataMasterProyek>

    private lateinit var proyekOptionAdapter: ArrayAdapter<DataMasterOption>
    private lateinit var jenisPermintaanOptionAdapter: ArrayAdapter<DataMasterOption>
    private lateinit var statusOptionAdapter: ArrayAdapter<DataMasterOption>
    private lateinit var jenisDataOptionAdapter: ArrayAdapter<DataMasterOption>

    private var spListState: Parcelable? = null
    private lateinit var spAdapter: BaseAdapter<MyDataViewHolder>

    override fun layoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapters()

        setupTambahSPDialog()
        setupFilterSPDialog()
        setupRecyclerView()
        setupListeners()

        initApiRequest()

        tv_show_length_entry.text = getString(
            R.string.main_header_list_count_msg,
            spAdapter.itemList.size.toString()
        )

        sharedViewModel.getOnNotifikasiReceived().observe(this, Observer {
            // val idSp = it
            initNotifikasiApiRequest()
        })

        sharedViewModel.getStatusFilterOptionList().observe(this, Observer {
            it?.forEach { item ->
                statusOptionList.add(item as DataMasterOption)
            }
            statusOptionAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getJenisDataFilterOptionList().observe(this, Observer {
            it?.forEach { item ->
                jenisDataOptionList.add(item as DataMasterOption)
            }
            jenisDataOptionAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getJenisPermintaanFilterOptionList().observe(this, Observer {
            it?.forEach { item ->
                jenisPermintaanOptionList.add(item as DataMasterOption)
            }
            jenisPermintaanOptionAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getProyekFilterOptionList().observe(this, Observer {
            it?.forEach { item ->
                proyekOptionList.add(item as DataMasterOption)
            }
            proyekOptionAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_DETAIL_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                when (data?.getStringExtra("status")) {
                    STATUS_SP_DELETED -> {
                        initApiRequest()
                    }
                }
            }
        }
    }

    private fun initNotifikasiApiRequest() {
        if (profileId != null) {
            disposable = notifikasiViewModel.getNotifikasiList(profileId.toString())
                .subscribe(this::handleResponse, this::handleError)
        }
    }

    private fun initApiRequest() {
        proyekList.clear()
        jenisList.clear()
        jenisPermintaanOptionList.clear()
        proyekOptionList.clear()
        statusOptionList.clear()
        jenisDataOptionList.clear()
        spAdapter.itemList.clear()

        tv_show_length_entry.text = getString(
            R.string.main_header_list_count_msg,
            "0"
        )

        proyekAdapter.notifyDataSetChanged()
        jenisAdapter.notifyDataSetChanged()
        jenisPermintaanOptionAdapter.notifyDataSetChanged()
        proyekOptionAdapter.notifyDataSetChanged()
        statusOptionAdapter.notifyDataSetChanged()
        jenisDataOptionAdapter.notifyDataSetChanged()
        spAdapter.notifyDataSetChanged()

        profileId = profilePreference.getProfile()?.id
        val roleId = profilePreference.getProfile()?.roleId

        if (profileId != null) {
            idUser = profileId.toString()

            // Master request api - START
            // -----------------------------------------------------------
            disposable = masterViewModel.getCostCodeList("all")
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getUomList("all")
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getPersyaratanList("all")
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getStatusFilterOptionList()
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getJenisDataFilterOptionList()
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getProyekFilterOptionList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getJenisPermintaanFilterOptionList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            // Master API END
            // -----------------------------------------------------------

            disposable = suratPermintaanViewModel.readMyData(
                idUser,
                selectedIdProyekFilterValue,
                selectedStatusFilterValue,
                selectedJenisPermintaanFilterValue,
                selectedJenisDataFilterValue
            )
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getProyekList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getJenisList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            disposable = notifikasiViewModel.getNotifikasiList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            startRefresh()
        }

        if (!roleId.equals("1")) {
            btnAjukan.visibility = View.GONE
        }
    }

    private fun initAdapters() {
        proyekAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, proyekList)

        jenisAdapter = ArrayAdapter(this, R.layout.material_spinner_item, jenisList)

        proyekOptionAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, proyekOptionList)

        jenisPermintaanOptionAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, jenisPermintaanOptionList)

        statusOptionAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, statusOptionList)

        jenisDataOptionAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, jenisDataOptionList)

        spAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_row, MyDataViewHolder::class.java
        )
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = spAdapter

        if (spListState != null)
            recyclerView.layoutManager?.onRestoreInstanceState(spListState)
    }

    private fun setupListeners() {
        btnProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        btnAjukan.setOnClickListener {
            alertDialogTambahSP.show()
        }

        btnFilter.setOnClickListener {
            alertDialogFilterSP.show()
        }

        frameNotifikasi.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotifikasiActivity::class.java))
        }

        spAdapter.setOnItemClickListener { item, _ ->
            val data = item as DataMyData
            val intent = Intent(this@MainActivity, DetailSuratPermintaanActivity::class.java)
            intent.putExtra(ID_SP_EXTRA_KEY, data.id.toString())
            startActivityForResult(intent, LAUNCH_DETAIL_ACTIVITY)
        }

        swipeRefreshLayout.setOnRefreshListener {
            if (!isConnectedToInternet) {
                toastNotify("Please turn on the internet")
                stopRefresh()
                return@setOnRefreshListener
            }

            initApiRequest()
        }
    }

    private fun startRefresh() {
        if (!isConnectedToInternet) return
        swipeRefreshLayout.isRefreshing = true
    }

    private fun stopRefresh() {
        Handler().postDelayed({ swipeRefreshLayout.isRefreshing = false }, 1000)
    }

    private fun handleResponse(response: Any) {
        stopRefresh()

        when (response) {
            is MyDataResponse -> {
                val suratPermintaanList: List<DataMyData?>? = response.data

                suratPermintaanList?.forEach {
                    spAdapter.itemList.add(it as DataMyData)
                }

                spAdapter.notifyDataSetChanged()

            }
            is MasterProyekResponse -> {

                response.data?.forEach {
                    if (it != null) {
                        proyekList.add(it)
                    }
                }
                proyekAdapter.notifyDataSetChanged()

            }
            is MasterJenisResponse -> {

                response.data?.forEach {
                    if (it != null) {
                        jenisList.add(it)
                    }
                }
                jenisAdapter.notifyDataSetChanged()

            }
            is NotifikasiResponse -> {
                val notif = response.data?.get(0)

                if (notif?.countUnread == 0) {
                    tvCountUnreadNotif.visibility = View.GONE
                } else {
                    tvCountUnreadNotif.visibility = View.VISIBLE
                    tvCountUnreadNotif.text = notif?.countUnread.toString()
                }
            }
            is CreateSPResponse -> {

                toastNotify(response.message)
                initApiRequest()

            }

            is MasterCCResponse -> {
                sharedViewModel.setCostCodeList(response.data)
            }

            is MasterUOMResponse -> {
                sharedViewModel.setUomList(response.data)
            }

            is MasterPersyaratanResponse -> {
                sharedViewModel.setPersyaratanList(response.data)
            }

            // Filter Option List
            // -----------------------------------------------------------
            is MasterStatusFilterOptionResponse -> {
                sharedViewModel.setStatusFilterOptionList(response.data)
            }

            is MasterJenisDataFilterOptionResponse -> {
                sharedViewModel.setJenisDataFilterOptionList(response.data)
            }

            is MasterProyekFilterOptionResponse -> {
                sharedViewModel.setProyekFilterOptionList(response.data)
            }

            is MasterJenisPermintaanFilterOptionResponse -> {
                sharedViewModel.setJenisPermintaanFilterOptionList(response.data)
            }
        }
    }

    override fun handleError(error: Throwable) {
        super.handleError(error)

        stopRefresh()
    }

    private fun setupTambahSPDialog() {
        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Ajukan Surat Permintaan")

        alertDialogTambahSP = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_ajukan_sp, null)

        dialogRootView.spinnerProyek.setAdapter(proyekAdapter)
        dialogRootView.spinnerJenis.setAdapter(jenisAdapter)
        dialogRootView.btnAjukan.setOnClickListener {

            val selectedProyek = dialogRootView.spinnerProyek.text.toString()
            val selectedJenis = dialogRootView.spinnerJenis.text.toString()

            var toastString = ""

            if (selectedProyek.isEmpty() || selectedJenis.isEmpty()) {
                toastString = "Silakan pilih "

                toastString = if (selectedProyek.isEmpty() xor selectedJenis.isEmpty()) {
                    if (selectedProyek.isEmpty()) {
                        "$toastString proyek"
                    } else {
                        "$toastString jenis"
                    }
                } else {
                    "$toastString proyek dan jenis"
                }
            }

            if (toastString.isNotEmpty()) {
                toastNotify(toastString)
                return@setOnClickListener
            }

            val idProyek = proyekList.find { it.nama == selectedProyek }?.id.toString()
            val namaJenis = jenisList.find { it.nama == selectedJenis }?.nama.toString()

            val confirmAlert = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menambah pengajuan?")
                .setPositiveButton("Ya") { _, _ ->

                    val createSP = CreateSP(idProyek, namaJenis, idUser)
                    disposable = suratPermintaanViewModel.add(createSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialogTambahSP.hide()

                    dialogRootView.spinnerProyek.text.clear()
                    dialogRootView.spinnerJenis.text.clear()

                    selectedJenisDataFilterValue = ""
                    selectedStatusFilterValue = ""
                    selectedIdProyekFilterValue = ""
                    selectedJenisPermintaanFilterValue = ""

                }.create()

            confirmAlert.show()
        }

        alertDialogTambahSP.setView(dialogRootView)
    }

    private fun setupFilterSPDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Ajukan Surat Permintaan")

        alertDialogFilterSP = alertDialogBuilder.create()

        val dialogRootView =
            View.inflate(this, R.layout.dialog_filter_sp, null)

        dialogRootView.spinnerProyek.setAdapter(proyekOptionAdapter)
        dialogRootView.spinnerJenis.setAdapter(jenisPermintaanOptionAdapter)
        dialogRootView.spinnerStatus.setAdapter(statusOptionAdapter)
        dialogRootView.spinnerJenisData.setAdapter(jenisDataOptionAdapter)

        dialogRootView.btnFilterSubmit.setOnClickListener {
            val selectedProyek = dialogRootView.spinnerProyek.text.toString()
            val selectedJenis = dialogRootView.spinnerJenis.text.toString()
            val selectedStatus = dialogRootView.spinnerStatus.text.toString()
            val selectedJenisData = dialogRootView.spinnerJenisData.text.toString()

            selectedIdProyekFilterValue =
                proyekOptionList.find { it.option == selectedProyek }?.value ?: ""
            selectedJenisPermintaanFilterValue =
                jenisPermintaanOptionList.find { it.option == selectedJenis }?.value ?: ""
            selectedStatusFilterValue =
                statusOptionList.find { it.option == selectedStatus }?.value ?: ""
            selectedJenisDataFilterValue =
                jenisDataOptionList.find { it.option == selectedJenisData }?.value ?: ""

            initApiRequest()

            alertDialogFilterSP.hide()
        }

        alertDialogFilterSP.setView(dialogRootView)
    }
}
