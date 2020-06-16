package com.example.e_suratpermintaan.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.e_suratpermintaan.core.domain.entities.responses.DataProfile
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.sharedpreference.ProfilePreference
import com.example.e_suratpermintaan.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import org.koin.android.ext.android.inject

class WebViewActivity : BaseActivity() {

    private var dataProfile: DataProfile? = null
    private lateinit var idUser: String
    private val profilePreference: ProfilePreference by inject()

    override fun layoutId(): Int = R.layout.activity_web_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataProfile = profilePreference.getProfile()

        dataProfile.let {
            val profileId = it!!.id
            if (profileId != null) {
                idUser = profileId
            }
            init()
        }
    }

    private fun init() {
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false

        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://jagat.jagatbuilding.co.id/master/master_data/?id=${idUser}")
    }

}
