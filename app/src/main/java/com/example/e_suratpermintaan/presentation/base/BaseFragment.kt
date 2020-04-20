package com.example.e_suratpermintaan.presentation.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment : Fragment() {

    private var disposableList: ArrayList<Disposable?> = ArrayList()

    var disposable: Disposable? = null
        set(value) {
            disposableList.add(value)
        }

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.isClickable = true
        view.isFocusable = true
        view.isFocusableInTouchMode = true

        findAndSetEditTextFocusChangeListenerRecursively(view)
    }

    override fun onDetach() {
        disposableList.forEach { disposable ->
            if (disposable != null) {
                if (!disposable.isDisposed) {
                    disposable.dispose()
                }
            }
        }

        super.onDetach()
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

    fun toastNotify(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}