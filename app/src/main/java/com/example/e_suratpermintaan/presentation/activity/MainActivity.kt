package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.LAUNCH_DETAIL_ACTIVITY
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.LAUNCH_EDIT_ACTIVITY
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.LAUNCH_PROFILE_ACTIVITY
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.STATUS_PROFILE_EDITED
import com.example.e_suratpermintaan.external.constants.IntentExtraConstants.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import com.example.e_suratpermintaan.presentation.base.BaseAdapter
import com.example.e_suratpermintaan.presentation.sharedlivedata.SharedMasterData
import com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter.MyDataViewHolder
import com.example.e_suratpermintaan.presentation.viewmodel.MasterViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.NotifikasiViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.ProfileViewModel
import com.example.e_suratpermintaan.presentation.viewmodel.SuratPermintaanViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.*
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.spinnerJenis
import kotlinx.android.synthetic.main.dialog_ajukan_sp.view.spinnerProyek
import kotlinx.android.synthetic.main.dialog_filter_sp.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var filterDialogRootView: View
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var profile: DataProfile? = null
    private lateinit var idUser: String
    private lateinit var roleId: String

    private var selectedStatusFilterValue: String = ""
    private var selectedJenisPermintaanFilterValue: String = ""
    private var selectedIdProyekFilterValue: String = ""

    private val profileViewModel: ProfileViewModel by viewModel()
    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val sharedMasterData: SharedMasterData by inject()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

    private lateinit var alertDialogTambahSP: AlertDialog
    private lateinit var alertDialogFilterSP: AlertDialog

    private val proyekList: ArrayList<DataMasterProyek> = arrayListOf()
    private val jenisList: ArrayList<DataMasterJenis> = arrayListOf()

    private val proyekOptionList: ArrayList<DataMasterOption> = arrayListOf()
    private val jenisPermintaanOptionList: ArrayList<DataMasterOption> = arrayListOf()
    private val statusOptionList: ArrayList<DataMasterOption> = arrayListOf()

    private lateinit var jenisAdapter: ArrayAdapter<DataMasterJenis>
    private lateinit var proyekAdapter: ArrayAdapter<DataMasterProyek>

    private lateinit var proyekOptionAdapter: ArrayAdapter<DataMasterOption>
    private lateinit var jenisPermintaanOptionAdapter: ArrayAdapter<DataMasterOption>
    private lateinit var statusOptionAdapter: ArrayAdapter<DataMasterOption>

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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onSuratPermintaanDataChange(suratPermintaanDataChange: SuratPermintaanDataChange) {
        if (suratPermintaanDataChange.changeType == SuratPermintaanDataChange.SP_DELETED) {
            initApiRequest()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LAUNCH_EDIT_ACTIVITY) {
                when (data?.getStringExtra("status")) {
                    STATUS_PROFILE_EDITED -> {
                        initDetailProfileRequest()
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

        profile = profilePreference.getProfile()
        idUser = profile?.id.toString()
        roleId = profile?.roleId.toString()

        setupNavigationDrawer()

        initDetailProfileRequest()
        populateMaster()

        initAdapters()
        setupRecyclerView()
        setupListeners()

        setupTambahSPDialog()
        setupFilterSPDialog()

        initApiRequest()
    }

    private fun populateMaster() {
        sharedMasterData.getOnNotifikasiReceived().observe(this, Observer {
            initNotifikasiApiRequest()
        })

        sharedMasterData.getStatusFilterOptionList().observe(this, Observer {
            statusOptionList.clear()
            it?.forEach { item ->
                statusOptionList.add(item as DataMasterOption)
            }
            statusOptionAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getProyekFilterOptionList()?.observe(this, Observer {
            if (proyekOptionList.size > 0) {
                Log.d(
                    "OBSERVER",
                    proyekOptionList.size.toString() + " ZOMBIE OBSERVER " + System.currentTimeMillis()
                        .toString()
                )
                proyekOptionList.clear()
            }

            it?.forEach { item ->
                proyekOptionList.add(item as DataMasterOption)
            }
            proyekOptionAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getJenisPermintaanFilterOptionList()?.observe(this, Observer {
            jenisPermintaanOptionList.clear()
            it?.forEach { item ->
                jenisPermintaanOptionList.add(item as DataMasterOption)
            }
            jenisPermintaanOptionAdapter.notifyDataSetChanged()
        })
    }

    private fun setupNavigationDrawer() {
        val menu: Menu = navigation_view.menu
        val menuItemMasterData: MenuItem = menu.findItem(R.id.menuMasterData)
        menuItemMasterData.isVisible = roleId == "0"
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
                    val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startActivityForResult(intent, LAUNCH_PROFILE_ACTIVITY)
                }
                R.id.logout -> {
                    profilePreference.removeProfile()
                    fcmPreference.removeUserTokenId()

                    finish()
                    startActivity(Intent(this, StarterActivity::class.java))
                }
                R.id.semua -> {
                    // indeks 0 untuk nilai valuenya "semua"
                    selectedStatusFilterValue = statusOptionList[0].value.toString()
                    val selectedStatusFilterOption = statusOptionList[0].option.toString()

                    filterDialogRootView.spinnerStatus.setText(selectedStatusFilterOption, false)
                    filterDialogRootView.tilStatus.visibility = View.VISIBLE
                    resetFilter(filterDialogRootView.tilStatus.visibility == View.VISIBLE)
                    initApiRequest()
                }
                R.id.menungguVerifikasi -> {
                    filterDialogRootView.tilStatus.visibility = View.GONE
                    resetFilter(filterDialogRootView.tilStatus.visibility == View.VISIBLE)
                    initApiRequest()
                }
                R.id.masterData -> {
                    val intent = Intent(this, WebViewActivity::class.java)
                    startActivity(intent)
                }
            }

            //This is for closing the drawer after acting on it
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

        initNavHeaderProfile()
    }

    private fun initNavHeaderProfile() {
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
        if (profile?.id != null) {
            disposable = notifikasiViewModel.getNotifikasiList(profile?.id.toString())
                .subscribe(this::handleResponse, this::handleError)
        }
    }

    private fun initDetailProfileRequest() {
        profileViewModel.getProfile(idUser).subscribe(
            { profileResponse ->

                var dataProfile: DataProfile? = DataProfile()

                profileResponse.data?.forEach { it ->
                    dataProfile = it
                }

                if (dataProfile != null) {
                    profilePreference.saveProfile(dataProfile)
                } else {
                    toastNotify(getString(R.string.profile_get_error_message))
                }

                initNavHeaderProfile()
            },
            { error ->
                toastNotify(error.message.toString())
            }
        )
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

        val roleId = profilePreference.getProfile()?.roleId
        if (!roleId.equals("1")) {
            btnAjukan.visibility = View.GONE
        }

        if (profile?.id != null) {

            disposable = suratPermintaanViewModel.readMyData(
                idUser,
                selectedIdProyekFilterValue,
                selectedStatusFilterValue,
                selectedJenisPermintaanFilterValue,
                ""
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

        spAdapter = BaseAdapter(
            R.layout.item_surat_permintaan_row, MyDataViewHolder::class.java
        )
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = spAdapter
        recyclerView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->

            if ((recyclerView.layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition() == spAdapter.itemList.size - 1
            ) {
                turnOffToolbarScrolling()
            } else {
                turnOnToolbarScrolling()
            }

        }

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
            initDetailProfileRequest()
        }

        tv_show_length_entry.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tv_show_length_entry.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val params = recyclerView.layoutParams
                params.height = deepCoordinatorLayout.height
                recyclerView.layoutParams = params
                recyclerView.requestLayout()
            }
        })
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
//                toastNotify(response.message)
                initApiRequest()

                val intent = Intent(this, EditSuratPermintaanActivity::class.java)
                intent.putExtra(EditSuratPermintaanActivity.ID_SP_EDIT, response.idSp.toString())
                startActivity(intent)
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
                .setTitle(getString(R.string.ajukan_sp_dialog_title))

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
                toastString = getString(R.string.must_choose_toast_msg)

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

            val idProyek = proyekList.find { it.option == selectedProyek }?.value.toString()
            val namaJenis = jenisList.find { it.nama == selectedJenis }?.nama.toString()

            val confirmAlert = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.confirmation_dialog_title))
                .setMessage(getString(R.string.pengajuan_confirmation_dialog_message))
                .setPositiveButton(getString(R.string.yes_btn_text)) { _, _ ->

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
                .setTitle(getString(R.string.filter_sp_dialog_title))

        alertDialogFilterSP = alertDialogBuilder.create()

        filterDialogRootView =
            View.inflate(this, R.layout.dialog_filter_sp, null)

        filterDialogRootView.spinnerProyek.setAdapter(proyekOptionAdapter)
        filterDialogRootView.spinnerJenis.setAdapter(jenisPermintaanOptionAdapter)
        filterDialogRootView.spinnerStatus.setAdapter(statusOptionAdapter)

        filterDialogRootView.spinnerStatus.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure you call it only once :
                filterDialogRootView.spinnerStatus.viewTreeObserver.removeOnGlobalLayoutListener(
                    this
                )
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

                filterDialogRootView.spinnerStatus.dropDownHeight =
                    dialogRootViewBottom - spinnerBottom
            }
        })

        filterDialogRootView.btnFilterReset.setOnClickListener {
            resetFilter(filterDialogRootView.tilStatus.visibility == View.VISIBLE)
        }

        filterDialogRootView.btnFilterSubmit.setOnClickListener {
            val selectedProyek = filterDialogRootView.spinnerProyek.text.toString()
            val selectedJenis = filterDialogRootView.spinnerJenis.text.toString()
            val selectedStatus = filterDialogRootView.spinnerStatus.text.toString()

            selectedIdProyekFilterValue =
                proyekOptionList.find { it.option == selectedProyek }?.value ?: ""
            selectedJenisPermintaanFilterValue =
                jenisPermintaanOptionList.find { it.option == selectedJenis }?.value ?: ""
            selectedStatusFilterValue =
                statusOptionList.find { it.option == selectedStatus }?.value ?: ""

            initApiRequest()

            alertDialogFilterSP.hide()
        }

        alertDialogFilterSP.setView(filterDialogRootView)
    }

    private fun resetFilter(isStatusPermintaanVisible: Boolean) {
        filterDialogRootView.spinnerProyek.text.clear()
        filterDialogRootView.spinnerJenis.text.clear()

        selectedIdProyekFilterValue = ""
        selectedJenisPermintaanFilterValue = ""

        if (!isStatusPermintaanVisible) {
            // ini maksudnya untuk kalau user memilih menu item menunggu verifikasi
            // nah status permintaannya kan ilang, jadi set statusFilterValue ke ""
            // karna untuk menunggu verifikasi statusPermintaanya default ""
            filterDialogRootView.spinnerStatus.text.clear()
            selectedStatusFilterValue = ""
        } else {
            selectedStatusFilterValue = statusOptionList[0].value.toString()
            val selectedStatusFilterOption = statusOptionList[0].option.toString()
            filterDialogRootView.spinnerStatus.setText(selectedStatusFilterOption, false)
        }
    }

    // https://stackoverflow.com/questions/30779667/android-collapsingtoolbarlayout-and-swiperefreshlayout-get-stuck/33776549
    // great solution : https://stackoverflow.com/a/30785823
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeRefreshLayout.isEnabled = (verticalOffset == 0)
    }

    override fun onResume() {
        super.onResume()
        listCountStatusBar.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        listCountStatusBar.removeOnOffsetChangedListener(this)
    }

    private fun turnOffToolbarScrolling() {
        //turn off scrolling
        val toolbarLayoutParams =
            listCountStatusContainer.layoutParams as AppBarLayout.LayoutParams
        toolbarLayoutParams.scrollFlags = 0
        listCountStatusContainer.layoutParams = toolbarLayoutParams
        val appBarLayoutParams =
            listCountStatusBar.layoutParams as CoordinatorLayout.LayoutParams
        appBarLayoutParams.behavior = null
        listCountStatusBar.layoutParams = appBarLayoutParams
    }

    private fun turnOnToolbarScrolling() {
        //turn on scrolling
        val toolbarLayoutParams =
            listCountStatusContainer.layoutParams as AppBarLayout.LayoutParams
        toolbarLayoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        listCountStatusContainer.layoutParams = toolbarLayoutParams
        val appBarLayoutParams =
            listCountStatusBar.layoutParams as CoordinatorLayout.LayoutParams
        appBarLayoutParams.behavior = AppBarLayout.Behavior()
        listCountStatusBar.layoutParams = appBarLayoutParams
    }
}
