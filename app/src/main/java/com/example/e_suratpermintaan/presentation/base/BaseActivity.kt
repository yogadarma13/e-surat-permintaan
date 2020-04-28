package com.example.e_suratpermintaan.presentation.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.presentation.activity.DetailSuratPermintaanActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.Disposable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

abstract class BaseActivity : AppCompatActivity() {

    private var disposableList: ArrayList<Disposable?> = ArrayList()
    var disposable: Disposable? = null
        set(value) {
            disposableList.add(value)
        }

    private val fcmOnMessageReceivedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val bodyValue = intent.getStringExtra("data_body_key")
            val idSP = intent.getStringExtra("id_sp")

            if (bodyValue != null) {
                snackNotify(getWindowContentRootView(), bodyValue, "Lihat", View.OnClickListener {
                    val activityIntent =
                        Intent(applicationContext, DetailSuratPermintaanActivity::class.java)
                    activityIntent.putExtra(DetailSuratPermintaanActivity.ID_SP_EXTRA_KEY, idSP)
                    startActivity(activityIntent)
                })
            }
        }
    }

    abstract fun layoutId(): Int

    private fun isJSONValid(string: String): Boolean{
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

        val responseBodyString = (error as HttpException).response()?.errorBody()?.string().toString()

        if (isJSONValid(responseBodyString)){
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

        window.decorView.clearFocus()
        window.decorView.setBackgroundColor(resources.getColor(android.R.color.background_light))
        closeKeyboard(this)
        findAndSetEditTextFocusChangeListenerRecursively(window.decorView)
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
                                    closeKeyboard(view)
                                } else {
                                    showKeyboard(view)
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

    fun closeKeyboard(activity: Activity) {
        val imm: InputMethodManager? =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun closeKeyboard(view: View) {
        val imm: InputMethodManager? =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        val imm: InputMethodManager? =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (!(imm?.isAcceptingText!!)) {
            imm.showSoftInput(view, 0)
        }
    }

}