package com.example.e_suratpermintaan.presentation.base

import android.annotation.TargetApi
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.e_suratpermintaan.core.domain.entities.connection.ConnectionModel
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.IntentExtraConstants.ID_SP_EXTRA_KEY
import com.example.e_suratpermintaan.framework.connection.ConnectionLiveData
import com.example.e_suratpermintaan.framework.connection.ConnectionLiveData.Companion.MobileData
import com.example.e_suratpermintaan.framework.connection.ConnectionLiveData.Companion.WifiData
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity
import com.example.e_suratpermintaan.presentation.shareddata.SharedMasterData
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.Disposable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.HttpException

abstract class BaseActivity : AppCompatActivity() {

    private val sharedMasterData: SharedMasterData by inject()

    var isConnectedToInternet: Boolean = true
    private lateinit var connectionLiveData: ConnectionLiveData
    private var disposableList: ArrayList<Disposable?> = ArrayList()
    var disposable: Disposable? = null
        set(value) {
            disposableList.add(value)
        }

    private val fcmOnMessageReceivedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val bodyValue: String? = intent.getStringExtra("data_body_key")
            val idSP: String? = intent.getStringExtra("id_sp")

            if (bodyValue.toString().isNotEmpty() && idSP.toString().isNotEmpty()) {
                sharedMasterData.setOnNotifikasiReceived(idSP.toString())

                snackNotify(getWindowContentRootView(), bodyValue!!, "Lihat", View.OnClickListener {
                    val activityIntent =
                        Intent(applicationContext, DetailSuratPermintaanActivity::class.java)
                    activityIntent.putExtra(ID_SP_EXTRA_KEY, idSP)
                    startActivity(activityIntent)
                })
            }
        }
    }

    abstract fun layoutId(): Int

    private fun isJSONValid(string: String): Boolean {
        try {
            JSONObject(string)
        } catch (ex: JSONException) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                JSONArray(string)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }

    open fun handleError(error: Throwable) {

        val responseBodyString =
            (error as HttpException).response()?.errorBody()?.string().toString()

        if (isJSONValid(responseBodyString)) {
            // Kalau format stringbody nya valid JSON, maka tampilkan atribut messagenya
            val jsonObject = JSONObject(responseBodyString)
            toastNotify(jsonObject.getString("message"))
        } else {
            // Kalau format stringbodynya gak valid JSON, maka tampilkan stringbody itu
            toastNotify(this::class.java.simpleName + ", HTTP ${error.code()} : " + responseBodyString)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())

        val filter = IntentFilter(getString(R.string.firebase_onmessagereceived_intentfilter))
        registerReceiver(fcmOnMessageReceivedReceiver, filter)

        // Ini dipakai biar supaya pas keyboard showup, gak ngepush view yang ada di activity
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        window.decorView.clearFocus()
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                android.R.color.background_light
            )
        )

        closeKeyboard(this)
        findAndSetEditTextFocusChangeListenerRecursively(window.decorView)
        setupInternetObserver()

        disableAutofill()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun disableAutofill() {
        window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
    }

    override fun onDestroy() {
        super.onDestroy()

        disposableList.forEach { disposable ->
            if (disposable != null) {
                if (!disposable.isDisposed) {
                    disposable.dispose()
                }
            }
        }

        unregisterReceiver(fcmOnMessageReceivedReceiver)
    }

    open fun getWindowContentRootView(): View {
        val contentViewGroup =
            findViewById<View>(android.R.id.content) as ViewGroup
        var rootView: View?
        rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = window.decorView.rootView
        return rootView
    }

    fun toastNotify(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun snackNotify(
        view: View,
        message: String,
        actionString: String?,
        onClickActionListener: View.OnClickListener?
    ) {
        //Snackbar(view)
        val snackbar = Snackbar.make(
            view, message,
            Snackbar.LENGTH_LONG
        )

        actionString.let {
            snackbar.setAction("Tutup") {
                snackbar.dismiss()
            }
            snackbar.setAction(actionString, onClickActionListener)
        }

        // snackbar.setActionTextColor(Color.BLACK)
        val snackView = snackbar.view
        // snackView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        // textView.setTextColor(Color.BLACK)
        textView.textSize = 14f
        snackbar.show()
    }

    fun findAndSetEditTextFocusChangeListenerRecursively(view: View) {
        for (i in 0 until (view as ViewGroup).childCount) {
            val child = view.getChildAt(i)

            if (child is ViewGroup) {
                findAndSetEditTextFocusChangeListenerRecursively(child)
            } else {
                if (child != null) {
                    if (child is EditText) {
                        setOnFocusChangeListener(
                            child,
                            View.OnFocusChangeListener { _, hasFocus ->
                                // Ketika tidak fokus pada edittext maka tutup keyboard/softinput
                                // ketika me-tap view diluar edittext maka akan menutup keyboard/softinput
                                if (!hasFocus) {
                                    closeKeyboard(child)
                                }
                            })
                    }
                }
            }
        }
    }

    private fun setOnFocusChangeListener(
        view: View,
        focusChangeListener: View.OnFocusChangeListener
    ) {
        view.onFocusChangeListener = focusChangeListener
    }

    private fun closeKeyboard(activity: Activity) {
        val imm: InputMethodManager? =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun closeKeyboard(view: View) {
        val imm: InputMethodManager? =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    fun showKeyboard(view: View) {
//        val imm: InputMethodManager? =
//            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//        if (!(imm?.isAcceptingText!!)) {
//            imm.showSoftInput(view, 0)
//        }
//    }

    private fun setupInternetObserver() {
        /* Live data object and setting an oberser on it */
        connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this,
            Observer<ConnectionModel?> { connection -> /* every time connection state changes, we'll be notified and can perform action accordingly */
                if (connection?.isConnected!!) {
                    isConnectedToInternet = true

                    when (connection.type) {
                        WifiData -> {
                        }
                        MobileData -> {
                        }
                    }
                } else {
                    isConnectedToInternet = false
                    toastNotify("Please turn on the internet")
                }
            })
    }

}