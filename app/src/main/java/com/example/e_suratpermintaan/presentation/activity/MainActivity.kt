package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity.Companion.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity.Companion.STATUS_SP_DELETED
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity.Companion.STATUS_SP_EDITED
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
import kotlinx.android.synthetic.main.nav_header.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    companion object {
        const val LAUNCH_DETAIL_ACTIVITY: Int = 111
    }

    private lateinit var filterDialogRootView: View
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var profileId: String? = null
    private lateinit var idUser: String

    private var selectedJenisDataFilterValue: String = ""
    private var selectedStatusFilterValue: String = ""
    private var selectedJenisPermintaanFilterValue: String = ""
    private var selectedIdProyekFilterValue: String = ""

    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by inject()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
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
                    STATUS_SP_EDITED -> {
                        initApiRequest()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                // empty
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }

        setSupportActionBar(toolbar)
        // This will display an Up icon (<-), we will replace it with hamburger later
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupNavigationDrawer()

        initAdapters()
        setupTambahSPDialog()
        setupFilterSPDialog()
        setupRecyclerView()
        setupListeners()

        initApiRequest()

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

        sharedViewModel.getProyekFilterOptionList().observe(this, Observer {
            it?.forEach { item ->
                proyekOptionList.add(item as DataMasterOption)
            }
            proyekOptionAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getJenisPermintaanFilterOptionList().observe(this, Observer {
            it?.forEach { item ->
                jenisPermintaanOptionList.add(item as DataMasterOption)
            }
            jenisPermintaanOptionAdapter.notifyDataSetChanged()
        })
    }

    private fun setupNavigationDrawer() {
        // Find our drawer view
        drawerToggle = setupDrawerToggle()

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        // Tie DrawerLayout events to the ActionBarToggle
        drawer_layout.addDrawerListener(drawerToggle)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.detail_profile -> {
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                }
                R.id.logout -> {
                    profilePreference.removeProfile()
                    fcmPreference.removeUserTokenId()

                    finish()
                    startActivity(Intent(this, StarterActivity::class.java))
                }
                R.id.semua -> {
                    // indeks 0 untuk nilai valuenya "semua"
                    resetFilter()
                    selectedStatusFilterValue = statusOptionList[0].value.toString()
                    filterDialogRootView.tilStatus.visibility = View.VISIBLE
                    initApiRequest()
                }
                R.id.menungguVerifikasi -> {
                    resetFilter()
                    // selectedStatusFilterValue = ""
                    filterDialogRootView.tilStatus.visibility = View.GONE
                    filterDialogRootView.spinnerStatus.text.clear()
                    initApiRequest()
                }
            }

            //This is for closing the drawer after acting on it
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        val headerView = navigation_view.getHeaderView(0)
        headerView.profileName.text = profilePreference.getProfile()?.name
        headerView.role.text = profilePreference.getProfile()?.namaRole
        headerView.email.text = profilePreference.getProfile()?.email
        Glide.with(this).load(profilePreference.getProfile()?.fotoProfile)
            .into(headerView.circleImageView)
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
    }

    private fun initNotifikasiApiRequest() {
        if (profileId != null) {
            disposable = notifikasiViewModel.getNotifikasiList(profileId.toString())
                .subscribe(this::handleResponse, this::handleError)
        }
    }

    private fun initApiRequest() {
        tv_show_length_entry.text = getString(
            R.string.main_header_list_count_msg,
            "0"
        )

        proyekList.clear()
        jenisList.clear()
        spAdapter.itemList.clear()

        proyekAdapter.notifyDataSetChanged()
        jenisAdapter.notifyDataSetChanged()
        spAdapter.notifyDataSetChanged()

        profileId = profilePreference.getProfile()?.id
        val roleId = profilePreference.getProfile()?.roleId

        if (!roleId.equals("1")) {
            btnAjukan.visibility = View.GONE
        }

        if (profileId != null) {
            idUser = profileId.toString()

            disposable = suratPermintaanViewModel.readMyData(
                idUser,
                selectedIdProyekFilterValue,
                selectedStatusFilterValue,
                selectedJenisPermintaanFilterValue,
                selectedJenisDataFilterValue
            )
                .subscribe(this::handleResponse, this::handleError)

            disposable = notifikasiViewModel.getNotifikasiList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getProyekList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            disposable = masterViewModel.getJenisList(idUser)
                .subscribe(this::handleResponse, this::handleError)

            startRefresh()
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
        Handler().postDelayed({ swipeRefreshLayout.isRefreshing = false }, 850)
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

                tv_show_length_entry.text = getString(
                    R.string.main_header_list_count_msg,
                    spAdapter.itemList.size.toString()
                )

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

                if (notif?.count == 0) {
                    tvCountUnreadNotif.visibility = View.GONE
                } else {
                    tvCountUnreadNotif.visibility = View.VISIBLE
                    tvCountUnreadNotif.text = notif?.count.toString()
                }
            }
            is CreateSPResponse -> {

                toastNotify(response.message)
                initApiRequest()

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

                }.create()

            confirmAlert.show()
        }

        alertDialogTambahSP.setView(dialogRootView)
    }

    private fun setupFilterSPDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Filter Surat Permintaan")

        alertDialogFilterSP = alertDialogBuilder.create()

        filterDialogRootView =
            View.inflate(this, R.layout.dialog_filter_sp, null)

        filterDialogRootView.spinnerProyek.setAdapter(proyekOptionAdapter)
        filterDialogRootView.spinnerJenis.setAdapter(jenisPermintaanOptionAdapter)
        filterDialogRootView.spinnerStatus.setAdapter(statusOptionAdapter)
        filterDialogRootView.spinnerJenisData.setAdapter(jenisDataOptionAdapter)

        filterDialogRootView.spinnerStatus.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure you call it only once :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    filterDialogRootView.spinnerStatus.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                //} else {
                    //dialogRootView.spinnerStatus.viewTreeObserver.removeGlobalOnLayoutListener(this)
                //}

                // Here you can get the size :)
                val spinnerCoord = intArrayOf(0, 0)
                filterDialogRootView.spinnerStatus.getLocationOnScreen(spinnerCoord)
                val spinnerBottom = spinnerCoord[1] + filterDialogRootView.spinnerStatus.height

                val dialogRootViewCoord = intArrayOf(0, 0)
                filterDialogRootView.getLocationOnScreen(dialogRootViewCoord)
                val dialogRootViewBottom = dialogRootViewCoord[1] + filterDialogRootView.height

                filterDialogRootView.spinnerStatus.dropDownHeight = dialogRootViewBottom - spinnerBottom
            }
        })

        filterDialogRootView.btnFilterReset.setOnClickListener {
            resetFilter()
        }

        filterDialogRootView.btnFilterSubmit.setOnClickListener {
            val selectedProyek = filterDialogRootView.spinnerProyek.text.toString()
            val selectedJenis = filterDialogRootView.spinnerJenis.text.toString()
            val selectedStatus = filterDialogRootView.spinnerStatus.text.toString()
            // val selectedJenisData = dialogRootView.spinnerJenisData.text.toString()

            selectedIdProyekFilterValue =
                proyekOptionList.find { it.option == selectedProyek }?.value ?: ""
            selectedJenisPermintaanFilterValue =
                jenisPermintaanOptionList.find { it.option == selectedJenis }?.value ?: ""
            selectedStatusFilterValue =
                statusOptionList.find { it.option == selectedStatus }?.value ?: ""
            // selectedJenisDataFilterValue = jenisDataOptionList.find { it.option == selectedJenisData }?.value ?: ""

            initApiRequest()

            alertDialogFilterSP.hide()
        }

        alertDialogFilterSP.setView(filterDialogRootView)
    }

    private fun resetFilter() {
        filterDialogRootView.spinnerProyek.text.clear()
        filterDialogRootView.spinnerJenis.text.clear()
        filterDialogRootView.spinnerStatus.text.clear()

        selectedIdProyekFilterValue = ""
        selectedJenisPermintaanFilterValue = ""
        selectedStatusFilterValue = ""
    }

}
