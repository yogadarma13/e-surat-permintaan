package com.example.e_suratpermintaan.presentation.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.requests.CreateSP
import com.e_suratpermintaan.core.domain.entities.responses.*
import com.e_suratpermintaan.core.domain.pojos.SuratPermintaanDataChange
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityMainBinding
import com.example.e_suratpermintaan.databinding.DialogAjukanSpBinding
import com.example.e_suratpermintaan.databinding.DialogFilterSpBinding
import com.example.e_suratpermintaan.databinding.ItemSuratPermintaanRowBinding
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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var dialogFilterSpBinding: DialogFilterSpBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var profile: DataProfile? = null
    private lateinit var idUser: String
    private lateinit var roleId: String

    private var selectedStatusFilterValue: String = "all"
    private var selectedJenisPermintaanFilterValue: String = "all"
    private var selectedIdProyekFilterValue: String = "all"
    private var isMyData: Boolean = true

    private val profileViewModel: ProfileViewModel by viewModel()
    private val suratPermintaanViewModel: SuratPermintaanViewModel by viewModel()
    private val notifikasiViewModel: NotifikasiViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    private val sharedMasterData: SharedMasterData by inject()
    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

    private lateinit var alertDialogTambahSP: AlertDialog
    private lateinit var alertDialogFilterSP: AlertDialog

    private val proyekList: ArrayList<DataMaster> = arrayListOf()
    private val jenisList: ArrayList<DataMaster> = arrayListOf()

    private val proyekOptionList: ArrayList<DataMaster> = arrayListOf()
    private val jenisPermintaanOptionList: ArrayList<DataMaster> = arrayListOf()
    private val statusOptionList: ArrayList<DataMaster> = arrayListOf()

    private lateinit var jenisAdapter: ArrayAdapter<DataMaster>
    private lateinit var proyekAdapter: ArrayAdapter<DataMaster>

    private lateinit var proyekOptionAdapter: ArrayAdapter<DataMaster>
    private lateinit var jenisPermintaanOptionAdapter: ArrayAdapter<DataMaster>
    private lateinit var statusOptionAdapter: ArrayAdapter<DataMaster>

    private var spListState: Parcelable? = null
    private lateinit var spAdapter: BaseAdapter<MyDataViewHolder, ItemSuratPermintaanRowBinding>

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                when (result.data?.getStringExtra("status")) {
                    STATUS_PROFILE_EDITED -> {
                        initDetailProfileRequest()
                    }
                }
            }
        }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

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
        if (suratPermintaanDataChange.changeType == SuratPermintaanDataChange.SP_DELETED ||
            suratPermintaanDataChange.changeType == SuratPermintaanDataChange.SP_BATAL ||
            suratPermintaanDataChange.changeType == SuratPermintaanDataChange.SP_AJUKAN ||
            suratPermintaanDataChange.changeType == SuratPermintaanDataChange.SP_VERIFIKASI ||
            suratPermintaanDataChange.changeType == SuratPermintaanDataChange.SP_TOLAK
        ) {
            initApiRequest()
            EventBus.getDefault().removeStickyEvent(suratPermintaanDataChange)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupToolbar()

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

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        // This will display an Up icon (<-), we will replace it with hamburger later
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun populateMaster() {
        sharedMasterData.getOnNotifikasiReceived().observe(this, {
            initNotifikasiApiRequest()
        })

        sharedMasterData.getStatusFilterOptionList().observe(this, {
            statusOptionList.clear()
            it?.forEach { item ->
                statusOptionList.add(item as DataMaster)
            }
            statusOptionAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getProyekFilterOptionList()?.observe(this, {
            if (proyekOptionList.size > 0) {
                Log.d(
                    "OBSERVER",
                    proyekOptionList.size.toString() + " ZOMBIE OBSERVER " + System.currentTimeMillis()
                        .toString()
                )
                proyekOptionList.clear()
            }

            it?.forEach { item ->
                proyekOptionList.add(item as DataMaster)
                proyekList.add(item)
            }
            proyekOptionAdapter.notifyDataSetChanged()
        })

        sharedMasterData.getJenisPermintaanFilterOptionList()?.observe(this, {
            jenisPermintaanOptionList.clear()
            it?.forEach { item ->
                jenisPermintaanOptionList.add(item as DataMaster)
            }
            jenisPermintaanOptionAdapter.notifyDataSetChanged()
        })
    }

    private fun setupNavigationDrawer() {
        val menu: Menu = binding.navigationView.menu
        val menuItemMasterData: MenuItem = menu.findItem(R.id.menuMasterData)
        menuItemMasterData.isVisible = roleId == "0"
        // Find our drawer view
        drawerToggle = setupDrawerToggle()

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()

        // Tie DrawerLayout events to the ActionBarToggle
        binding.drawerLayout.addDrawerListener(drawerToggle)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.detail_profile -> {
                    val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startForResult.launch(intent)
                }
                R.id.logout -> {
                    logout()
                }
                R.id.semua -> {
                    isMyData = false
                    // indeks 0 untuk nilai valuenya "semua"
                    selectedStatusFilterValue = statusOptionList[0].value.toString()
                    val selectedStatusFilterOption = statusOptionList[0].option.toString()

                    dialogFilterSpBinding.spinnerStatus.setText(selectedStatusFilterOption, false)
                    dialogFilterSpBinding.tilStatus.visibility = View.VISIBLE
                    resetFilter(dialogFilterSpBinding.tilStatus.visibility == View.VISIBLE)
                    initApiRequest()
                }
                R.id.menungguVerifikasi -> {
                    isMyData = true
                    dialogFilterSpBinding.tilStatus.visibility = View.GONE
                    resetFilter(dialogFilterSpBinding.tilStatus.visibility == View.VISIBLE)
                    initApiRequest()
                }
                R.id.masterData -> {
                    val intent = Intent(this, WebViewActivity::class.java)
                    startActivity(intent)
                }
            }

            //This is for closing the drawer after acting on it
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        initNavHeaderProfile()
    }

    private fun logout() {
        profilePreference.removeProfile()
        fcmPreference.removeUserTokenId()

        finish()
        startActivity(Intent(this, StarterActivity::class.java))
    }

    private fun initNavHeaderProfile() {
        val headerView = binding.navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.profileName).text =
            profilePreference.getProfile()?.name
        headerView.findViewById<TextView>(R.id.role).text = profilePreference.getProfile()?.namaRole
        headerView.findViewById<TextView>(R.id.username).text = profilePreference.getProfile()?.username
        Glide.with(this).load(profilePreference.getProfile()?.fotoProfile)
            .into(headerView.findViewById(R.id.circleImageView))
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
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
        binding.tvShowLengthEntry.text = getString(
            R.string.main_header_list_count_msg,
            "0"
        )

//        proyekList.clear()
        jenisList.clear()
        spAdapter.itemList.clear()

        proyekAdapter.notifyDataSetChanged()
        jenisAdapter.notifyDataSetChanged()
        spAdapter.notifyDataSetChanged()

        val roleId = profilePreference.getProfile()?.roleId
        if (!roleId.equals("1")) {
            binding.btnAjukan.visibility = View.GONE
        }

        if (profile?.id != null) {

            disposable = if (isMyData) {
                suratPermintaanViewModel.readMyData(
                    idUser, selectedIdProyekFilterValue, selectedJenisPermintaanFilterValue
                )
                    .subscribe(this::handleResponse, this::handleError)
            } else {
                suratPermintaanViewModel.readAllData(
                    idUser,
                    selectedIdProyekFilterValue,
                    selectedJenisPermintaanFilterValue,
                    selectedStatusFilterValue
                ).subscribe(this::handleResponse, this::handleError)
            }

            disposable = notifikasiViewModel.getNotifikasiList(idUser)
                .subscribe(this::handleResponse, this::handleError)

//            disposable = masterViewModel.getProyekList(idUser)
//                .subscribe(this::handleResponse, this::handleError)

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
            ItemSuratPermintaanRowBinding::inflate, MyDataViewHolder::class.java
        )
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = spAdapter
            addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->

                if ((this.layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition() == spAdapter.itemList.size - 1
                ) {
                    turnOffToolbarScrolling()
                } else {
                    turnOnToolbarScrolling()
                }

            }
        }


        if (spListState != null)
            binding.recyclerView.layoutManager?.onRestoreInstanceState(spListState)
    }

    private fun setupListeners() {

        binding.btnAjukan.setOnClickListener {
            alertDialogTambahSP.show()
        }

        binding.btnFilter.setOnClickListener {
            alertDialogFilterSP.show()
        }

        binding.frameNotifikasi.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotifikasiActivity::class.java))
        }

        spAdapter.setOnItemClickListener { item, _ ->
            val data = item as DataSuratPermintaan
            val intent = Intent(this@MainActivity, DetailSuratPermintaanActivity::class.java)
            intent.putExtra(ID_SP_EXTRA_KEY, data.id.toString())
            startActivity(intent)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!isConnectedToInternet) {
                toastNotify("Please turn on the internet")
                stopRefresh()
                return@setOnRefreshListener
            }

            initApiRequest()
            initDetailProfileRequest()
        }

        binding.tvShowLengthEntry.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.tvShowLengthEntry.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val params = binding.recyclerView.layoutParams
                params.height = binding.deepCoordinatorLayout.height
                binding.recyclerView.layoutParams = params
                binding.recyclerView.requestLayout()
            }
        })
    }

    private fun startRefresh() {
        if (!isConnectedToInternet) return
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun stopRefresh() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeRefreshLayout.isRefreshing = false
        }, 850)
    }

    private fun handleResponse(response: Any) {
        stopRefresh()

        when (response) {
            is MyDataResponse -> {
                spAdapter.itemList.clear()

                val suratPermintaanList: List<DataSuratPermintaan?>? = response.data

                suratPermintaanList?.forEach {
                    spAdapter.itemList.add(it as DataSuratPermintaan)
                }

                spAdapter.notifyDataSetChanged()

                binding.tvShowLengthEntry.text = getString(
                    R.string.main_header_list_count_msg,
                    spAdapter.itemList.size.toString()
                )

            }

            is DataAllResponse -> {
                spAdapter.itemList.clear()

                val suratPermintaanList: List<DataSuratPermintaan?>? = response.data

                suratPermintaanList?.forEach {
                    spAdapter.itemList.add(it as DataSuratPermintaan)
                }

                spAdapter.notifyDataSetChanged()

                binding.tvShowLengthEntry.text = getString(
                    R.string.main_header_list_count_msg,
                    spAdapter.itemList.size.toString()
                )

            }
//            is MasterProyekResponse -> {
//
//                response.data?.forEach {
//                    if (it != null) {
//                        proyekList.add(it)
//                    }
//                }
//                proyekAdapter.notifyDataSetChanged()
//
//            }
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
                    binding.tvCountUnreadNotif.visibility = View.GONE
                } else {
                    binding.tvCountUnreadNotif.visibility = View.VISIBLE
                    binding.tvCountUnreadNotif.text = notif?.count.toString()
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

        val dialogAjukanSpBinding = DialogAjukanSpBinding.inflate(LayoutInflater.from(this))

        dialogAjukanSpBinding.spinnerProyek.setAdapter(proyekAdapter)
        dialogAjukanSpBinding.spinnerJenis.setAdapter(jenisAdapter)
        dialogAjukanSpBinding.btnAjukan.setOnClickListener {

            val selectedProyek = dialogAjukanSpBinding.spinnerProyek.text.toString()
            val selectedJenis = dialogAjukanSpBinding.spinnerJenis.text.toString()

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
            val namaJenis = jenisList.find { it.option == selectedJenis }?.option.toString()

            val confirmAlert = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.confirmation_dialog_title))
                .setMessage(getString(R.string.pengajuan_confirmation_dialog_message))
                .setPositiveButton(getString(R.string.yes_btn_text)) { _, _ ->

                    val createSP = CreateSP(idProyek, namaJenis, idUser)
                    disposable = suratPermintaanViewModel.add(createSP)
                        .subscribe(this::handleResponse, this::handleError)

                    alertDialogTambahSP.hide()

                    dialogAjukanSpBinding.spinnerProyek.text.clear()
                    dialogAjukanSpBinding.spinnerJenis.text.clear()

                }.create()

            confirmAlert.show()
        }

        alertDialogTambahSP.setView(dialogAjukanSpBinding.root)
    }

    private fun setupFilterSPDialog() {

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.filter_sp_dialog_title))

        alertDialogFilterSP = alertDialogBuilder.create()

        dialogFilterSpBinding = DialogFilterSpBinding.inflate(LayoutInflater.from(this))

        dialogFilterSpBinding.spinnerProyek.setAdapter(proyekOptionAdapter)
        dialogFilterSpBinding.spinnerJenis.setAdapter(jenisPermintaanOptionAdapter)
        dialogFilterSpBinding.spinnerStatus.setAdapter(statusOptionAdapter)

        dialogFilterSpBinding.spinnerStatus.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Ensure you call it only once :
                dialogFilterSpBinding.spinnerStatus.viewTreeObserver.removeOnGlobalLayoutListener(
                    this
                )
                //} else {
                //dialogRootView.spinnerStatus.viewTreeObserver.removeGlobalOnLayoutListener(this)
                //}

                // Here you can get the size :)
                val spinnerCoord = intArrayOf(0, 0)
                dialogFilterSpBinding.spinnerStatus.getLocationOnScreen(spinnerCoord)
                val spinnerBottom = spinnerCoord[1] + dialogFilterSpBinding.spinnerStatus.height

                val dialogRootViewCoord = intArrayOf(0, 0)
                dialogFilterSpBinding.root.getLocationOnScreen(dialogRootViewCoord)
                val dialogRootViewBottom =
                    dialogRootViewCoord[1] + dialogFilterSpBinding.root.height

                dialogFilterSpBinding.spinnerStatus.dropDownHeight =
                    dialogRootViewBottom - spinnerBottom
            }
        })

        dialogFilterSpBinding.btnFilterReset.setOnClickListener {
            resetFilter(dialogFilterSpBinding.tilStatus.visibility == View.VISIBLE)
        }

        dialogFilterSpBinding.btnFilterSubmit.setOnClickListener {
            val selectedProyek = dialogFilterSpBinding.spinnerProyek.text.toString()
            val selectedJenis = dialogFilterSpBinding.spinnerJenis.text.toString()
            val selectedStatus = dialogFilterSpBinding.spinnerStatus.text.toString()

            selectedIdProyekFilterValue =
                proyekOptionList.find { it.option == selectedProyek }?.value ?: "all"
            selectedJenisPermintaanFilterValue =
                jenisPermintaanOptionList.find { it.option == selectedJenis }?.value ?: "all"
            selectedStatusFilterValue =
                statusOptionList.find { it.option == selectedStatus }?.value ?: "all"

            initApiRequest()

            alertDialogFilterSP.dismiss()
        }

        alertDialogFilterSP.setView(dialogFilterSpBinding.root)
    }

    private fun resetFilter(isStatusPermintaanVisible: Boolean) {
        dialogFilterSpBinding.spinnerProyek.text.clear()
        dialogFilterSpBinding.spinnerJenis.text.clear()

        selectedIdProyekFilterValue = "all"
        selectedJenisPermintaanFilterValue = "all"

        if (!isStatusPermintaanVisible) {
            // ini maksudnya untuk kalau user memilih menu item menunggu verifikasi
            // nah status permintaannya kan ilang, jadi set statusFilterValue ke ""
            // karna untuk menunggu verifikasi statusPermintaanya default ""
            dialogFilterSpBinding.spinnerStatus.text.clear()
            selectedStatusFilterValue = "all"
        } else {
            selectedStatusFilterValue = statusOptionList[0].value.toString()
            val selectedStatusFilterOption = statusOptionList[0].option.toString()
            dialogFilterSpBinding.spinnerStatus.setText(selectedStatusFilterOption, false)
        }
    }

    // https://stackoverflow.com/questions/30779667/android-collapsingtoolbarlayout-and-swiperefreshlayout-get-stuck/33776549
    // great solution : https://stackoverflow.com/a/30785823
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        binding.swipeRefreshLayout.isEnabled = (verticalOffset == 0)
    }

    override fun onResume() {
        super.onResume()
        binding.listCountStatusBar.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding.listCountStatusBar.removeOnOffsetChangedListener(this)
    }

    private fun turnOffToolbarScrolling() {
        //turn off scrolling
        val toolbarLayoutParams =
            binding.listCountStatusContainer.layoutParams as AppBarLayout.LayoutParams
        toolbarLayoutParams.scrollFlags = 0
        binding.listCountStatusContainer.layoutParams = toolbarLayoutParams
        val appBarLayoutParams =
            binding.listCountStatusBar.layoutParams as CoordinatorLayout.LayoutParams
        appBarLayoutParams.behavior = null
        binding.listCountStatusBar.layoutParams = appBarLayoutParams
    }

    private fun turnOnToolbarScrolling() {
        //turn on scrolling
        val toolbarLayoutParams =
            binding.listCountStatusContainer.layoutParams as AppBarLayout.LayoutParams
        toolbarLayoutParams.scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        binding.listCountStatusContainer.layoutParams = toolbarLayoutParams
        val appBarLayoutParams =
            binding.listCountStatusBar.layoutParams as CoordinatorLayout.LayoutParams
        appBarLayoutParams.behavior = AppBarLayout.Behavior()
        binding.listCountStatusBar.layoutParams = appBarLayoutParams
    }
}
