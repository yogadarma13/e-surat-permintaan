package com.example.e_suratpermintaan.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.e_suratpermintaan.core.domain.entities.responses.DataMaster
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ActivityWebViewBinding
import com.example.e_suratpermintaan.external.constants.ActivityResultConstants.LAUNCH_PROFILE_ACTIVITY
import com.example.e_suratpermintaan.framework.sharedpreference.FCMPreference
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import org.koin.android.ext.android.inject

class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    private var url = ""
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var profile: DataProfile? = null
    private lateinit var idUser: String
    private lateinit var roleId: String

    private val profilePreference: ProfilePreference by inject()
    private val fcmPreference: FCMPreference by inject()

    private val statusOptionList: ArrayList<DataMaster> = arrayListOf()

    override fun getViewBinding(): ActivityWebViewBinding =
        ActivityWebViewBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profile = profilePreference.getProfile()

        idUser = profile?.id.toString()
        roleId = profile?.roleId.toString()

        url = "https://jagat.jagatbuilding.co.id/master/master_data/?id=${idUser}"
        init()
    }

    private fun init() {
        setupTollbar()
        setupNavigationDrawer()

        val client = CustomWebViewClient()

        with(binding.webView) {
            settings.loadsImagesAutomatically = true
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false

            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            webViewClient = client
            url?.let { loadUrl(it) }
        }
    }

    private fun setupTollbar() {
        binding.toolbarMasterData.text = getString(R.string.toolbar_master_data)
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
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
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivityForResult(intent, LAUNCH_PROFILE_ACTIVITY)
                }
                R.id.logout -> {
                    profilePreference.removeProfile()
                    fcmPreference.removeUserTokenId()

                    finish()
                    startActivity(Intent(this, StarterActivity::class.java))
                }
                R.id.semua -> {
                    finish()
                }
                R.id.menungguVerifikasi -> {
                    finish()
                }
                R.id.masterData -> {
                }
            }

            //This is for closing the drawer after acting on it
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        initNavHeaderProfile()
    }

    private fun initNavHeaderProfile() {
        val headerView = binding.navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.profileName).text =
            profilePreference.getProfile()?.name
        headerView.findViewById<TextView>(R.id.role).text = profilePreference.getProfile()?.namaRole
        headerView.findViewById<TextView>(R.id.email).text = profilePreference.getProfile()?.email
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.webView.canGoBack()) {
                        if (!DetectConnection().checkConnection(this)) {
                            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                            alertDialogBuilder.setTitle("Koneksi terputus")
                            alertDialogBuilder.setMessage("Koneksi internet anda terputus")
                            alertDialogBuilder.setPositiveButton("Kembali ke menu") { _, _ -> this.finish() }
                            alertDialogBuilder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                        } else {
                            binding.webView.goBack()
                        }

                    } else {
                        finish()
                    }
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    class CustomWebViewClient : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (!DetectConnection().checkConnection(view?.context!!)) {
                Toast.makeText(view.context!!, "Please turn on the internet", Toast.LENGTH_LONG)
                    .show()
            } else {
                val url = request?.url.toString()
                view.loadUrl(url)
            }
            return true
        }
    }

    class DetectConnection {
        fun checkConnection(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                return nwInfo.isConnected
            }
        }
    }
}
