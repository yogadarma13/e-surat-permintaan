package com.example.e_suratpermintaan.presentation.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    private var disposableList: ArrayList<Disposable?> = ArrayList()
    var disposable: Disposable? = null
        set(value) {
            disposableList.add(value)
        }

    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())

        window.decorView.clearFocus()
        window.decorView.setBackgroundColor(resources.getColor(android.R.color.background_light))
        closeKeyboard(this)
        findAndSetEditTextFocusChangeListenerRecursively(window.decorView)
    }

    fun toastNotify(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun findAndSetEditTextFocusChangeListenerRecursively(view: View) {
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

    private fun closeKeyboard(view: View) {
        val imm: InputMethodManager? =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm: InputMethodManager? =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (!(imm?.isAcceptingText!!)) {
            imm.showSoftInput(view, 0)
        }
    }

}