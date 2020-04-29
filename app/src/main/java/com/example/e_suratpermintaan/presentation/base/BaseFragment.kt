package com.example.e_suratpermintaan.presentation.base

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.e_suratpermintaan.R
import io.reactivex.rxjava3.disposables.Disposable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

abstract class BaseFragment : Fragment() {

    private var isConfigChanges = false
    private var disposableList: ArrayList<Disposable?> = ArrayList()

    var disposable: Disposable? = null
        set(value) {
            disposableList.add(value)
        }

    abstract fun layoutId(): Int

    private var rootView: View? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, layoutId())
    }

    private fun getPersistentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        layout: Int
    ): View? {
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater?.inflate(layout, container, false)
            Log.d("MYAPP", "CREATE NEW ROOTVIEW")
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
            Log.d("MYAPP", "USE EXISTING ROOTVIEW")
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MYAPP", "ON VIEW CREATED")

        view.isClickable = true
        view.isFocusable = true
        view.isFocusableInTouchMode = true
    }

    // Ini selalu dijalankan setiap mau menampilkan fragment, baik fragment yang udah pernah ditampilkan
    // atupun fragment yang baru mau ditampilkan
    // https://stackoverflow.com/questions/17792132/how-does-onviewstaterestored-from-fragments-work
    // onViewStateRestored dipanggil setelah onCreateView() dan sebelum onResume()
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("MYAPP", "ON VIEW RESTORED")

        // Ini harus dipanggil karna saat orientation change atau config change onCreateAnimation itu gak dipanggil
        if (savedInstanceState?.getBoolean("is_config_change") == true) {
            Log.d("MYAPP", "ON ENTER ANIMATION END")
            onEnterAnimationEnd()
            isConfigChanges = false
        }

        anyInitName()
    }

    // Ini merupakan method yang bakalan dijalankan lebih dulu daripada initApiRequest
    // Method init() bakalan yang selalu dijalankan di awal (bukan pertama) meskipun fragment baru
    // init() hanya dipanggil di onViewStateRestored
    private fun anyInitName() {

    }

    open fun saveState() {
        Log.d("MYAPP", "SAVE STATE")
    }

    open fun clearState() {
        Log.d("MYAPP", "CLEAR STATE")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("MYAPP", "ON SAVE INSTANCE STATE")

        outState.putBoolean("is_config_change", true)
        isConfigChanges = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MYAPP", "ON DESTROY VIEW")

        saveState()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MYAPP", "ON DESTROY")

        rootView = null

        if (!isConfigChanges) {
            // Kalau configchanges jangan clearState karna configchanges itu menjalankan lifecycle
            // sampai ke onDetach
            clearState()
        }
    }

    override fun onDetach() {
        Log.d("MYAPP", "ON DETACH")

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

    // Gunakan method ini untuk mulai malankan perintah seperti getdata dengan rxjava,
    // set adapter ke recyclerview, dan lain-lain
    // Jadi gak memblock (freeze) enter animation si fragment
    // Kode dijalankan setelah animasi selesai / berhenti
    open fun onEnterAnimationEnd() {
        requireView().clearFocus()
        requireView().setBackgroundColor(resources.getColor(android.R.color.background_light))
        findAndSetEditTextFocusChangeListenerRecursively(requireView())
    }

    // Sekedar racikan sederhana animasi transisi fragment untuk navigation component
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        //https://stackoverflow.com/questions/19614392/fragmenttransaction-before-and-after-setcustomanimation-callback
        //Check if the superclass already created the animation
        Log.d("MYAPP", "ON CREATE ANIMATION")
        var anim = super.onCreateAnimation(transit, enter, nextAnim)

        // This is just any good resource to read
        // https://stackoverflow.com/a/54086704

        anim = handleOnEnterAnimationEnd(anim, nextAnim, enter)

        handleOnExitForegroundDimAnimation(enter, nextAnim)

        return anim
    }

    private fun handleOnEnterAnimationEnd(
        anim: Animation?,
        nextAnim: Int,
        enter: Boolean
    ): Animation? {

        //If not, and an animation is defined, load it now
        var tempAnim = anim

        if (tempAnim == null && nextAnim !== 0) {
            tempAnim = AnimationUtils.loadAnimation(activity, nextAnim)
        }

        //If there is an animation for this fragment, add a listener.
        tempAnim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                // Bisa dijamin onAnimationEnd ini dijalankan paling akhir setelah semua
                // fragment lifecycle (sampai onResume) selesai

                if (enter) {
                    // Jalankan ketika giliran fragment ditampilkan ke user
                    onEnterAnimationEnd()
                }

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        return tempAnim
    }

    private fun handleOnExitForegroundDimAnimation(enter: Boolean, nextAnim: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!enter) {
                if (nextAnim == R.anim.slide_out_to_left) {
                    val color: Int = android.R.color.black
                    view?.foreground =
                        ColorDrawable(ContextCompat.getColor(requireContext(), color))
                    view?.foreground?.alpha = 0

                    val animator =
                        ObjectAnimator.ofInt(requireView().foreground, "alpha", 0, 200)
                    animator.duration = 400
                    animator.startDelay = 0
                    animator.start()
                }
            } else {
                if (requireView().foreground != null) {
                    if (requireView().foreground.alpha == 200) {
                        val animator =
                            ObjectAnimator.ofInt(requireView().foreground, "alpha", 10, 0)
                        animator.duration = 150
                        animator.start()
                    }
                }
            }
        }
    }
}