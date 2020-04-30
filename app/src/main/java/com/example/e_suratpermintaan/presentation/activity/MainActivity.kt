package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
    private var selectedNamaJenisFilterValue: String = ""
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

    private val proyekPermintaanFilterList: ArrayList<DataMasterProyek> = arrayListOf()
    private val jenisPermintaanFilterList: ArrayList<DataMasterJenis> = arrayListOf()
    private val statusPermintaanFilterList: ArrayList<DataMasterStatusPermintaan> = arrayListOf()
    private val jenisDataPermintaanFilterList: ArrayList<DataMasterJenisDataPermintaan> =
        arrayListOf()

    private lateinit var jenisAdapter: ArrayAdapter<DataMasterJenis>
    private lateinit var proyekAdapter: ArrayAdapter<DataMasterProyek>

    private lateinit var jenisPermintaanFilterAdapter: ArrayAdapter<DataMasterJenis>
    private lateinit var proyekPermintaanFilterAdapter: ArrayAdapter<DataMasterProyek>
    private lateinit var statusPermintaanFilterAdapter: ArrayAdapter<DataMasterStatusPermintaan>
    private lateinit var jenisDataPermintaanFilterAdapter: ArrayAdapter<DataMasterJenisDataPermintaan>

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

        sharedViewModel.getStatusPermintaanList().observe(this, Observer {
            it?.forEach { item ->
                statusPermintaanFilterList.add(item as DataMasterStatusPermintaan)
            }
            statusPermintaanFilterAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getJenisDataPermintaanList().observe(this, Observer {
            it?.forEach { item ->
                jenisDataPermintaanFilterList.add(item as DataMasterJenisDataPermintaan)
            }
            jenisDataPermintaanFilterAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_DETAIL_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                when (data?.getStringExtra("status")) {
                    STATUS_SP_DELETED -> {

                        tv_show_length_entry.text = getString(
                            R.string.main_header_list_count_msg,
                            "0"
                        )

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
        jenisPermintaanFilterList.clear()
        proyekPermintaanFilterList.clear()
        statusPermintaanFilterList.clear()
        jenisDataPermintaanFilterList.clear()
        spAdapter.itemList.clear()

        proyekAdapter.notifyDataSetChanged()
        jenisAdapter.notifyDataSetChanged()
        jenisPermintaanFilterAdapter.notifyDataSetChanged()
        proyekPermintaanFilterAdapter.notifyDataSetChanged()
        statusPermintaanFilterAdapter.notifyDataSetChanged()
        jenisDataPermintaanFilterAdapter.notifyDataSetChanged()
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

            disposable = masterViewModel.getStatusPermintaanList()
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getJenisDataPermintaanList()
                .subscribe(this::handleResponse, this::handleError)
            // Master API END
            // -----------------------------------------------------------

            disposable = suratPermintaanViewModel.readMyData(
                idUser,
                selectedIdProyekFilterValue,
                selectedStatusFilterValue,
                selectedNamaJenisFilterValue,
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

        proyekPermintaanFilterAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, proyekPermintaanFilterList)

        jenisPermintaanFilterAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, jenisPermintaanFilterList)

        statusPermintaanFilterAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, statusPermintaanFilterList)

        jenisDataPermintaanFilterAdapter =
            ArrayAdapter(this, R.layout.material_spinner_item, jenisDataPermintaanFilterList)

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
        swipeRefreshLayout.isRefreshing = false
    }

    private fun handleResponse(response: Any) {
        when (response) {
            is MyDataResponse -> {

                stopRefresh()
                val suratPermintaanList: List<DataMyData?>? = response.data

                suratPermintaanList?.forEach {
                    spAdapter.itemList.add(it as DataMyData)
                }

                tv_show_length_entry.text = getString(
                    R.string.main_header_list_count_msg,
                    spAdapter.itemList.size.toString()
                )
                spAdapter.notifyDataSetChanged()

            }
            is MasterProyekResponse -> {

                proyekPermintaanFilterList.add(DataMasterProyek("Semua Proyek", ""))
                response.data?.forEach {
                    if (it != null) {
                        proyekPermintaanFilterList.add(it)
                        proyekList.add(it)
                    }
                }
                proyekAdapter.notifyDataSetChanged()

            }
            is MasterJenisResponse -> {

                jenisPermintaanFilterList.add(DataMasterJenis("Semua Jenis", ""))
                response.data?.forEach {
                    if (it != null) {
                        jenisPermintaanFilterList.add(it)
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

            is MasterStatusPermintaanResponse -> {
                sharedViewModel.setStatusPermintaanList(response.data)
            }

            is MasterJenisDataPermintaanResponse -> {
                sharedViewModel.setJenisDataPermintaanList(response.data)
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

            alertDialogTambahSP = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menambah pengajuan?")
                .setPositiveButton("Ya") { _, _ ->

                    val createSP = CreateSP(idProyek, namaJenis, idUser)
                    disposable = suratPermintaanViewModel.add(createSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialogTambahSP.hide()

                }.create()

            alertDialogTambahSP.show()
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

        dialogRootView.spinnerProyek.setAdapter(proyekPermintaanFilterAdapter)
        dialogRootView.spinnerJenis.setAdapter(jenisPermintaanFilterAdapter)
        dialogRootView.spinnerStatus.setAdapter(statusPermintaanFilterAdapter)
        dialogRootView.spinnerJenisData.setAdapter(jenisDataPermintaanFilterAdapter)

        dialogRootView.btnFilterSubmit.setOnClickListener {
            val selectedProyek = dialogRootView.spinnerProyek.text.toString()
            val selectedJenis = dialogRootView.spinnerJenis.text.toString()
            val selectedStatus = dialogRootView.spinnerStatus.text.toString()
            val selectedJenisData = dialogRootView.spinnerJenisData.text.toString()

            selectedIdProyekFilterValue =
                proyekPermintaanFilterList.find { it.nama == selectedProyek }?.id.toString()
            selectedNamaJenisFilterValue =
                jenisPermintaanFilterList.find { it.nama == selectedJenis }?.nama.toString()
            selectedStatusFilterValue =
                statusPermintaanFilterList.find { it.option == selectedStatus }?.value.toString()
            selectedJenisDataFilterValue =
                jenisDataPermintaanFilterList.find { it.option == selectedJenisData }?.value.toString()

            initApiRequest()
            startRefresh()

            alertDialogFilterSP.hide()
        }

        alertDialogFilterSP.setView(dialogRootView)
    }
}
