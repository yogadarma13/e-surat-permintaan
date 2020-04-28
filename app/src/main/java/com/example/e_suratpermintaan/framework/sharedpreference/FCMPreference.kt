package com.example.e_suratpermintaan.framework.sharedpreference

import android.content.Context
import com.example.e_suratpermintaan.external.constants.PreferenceConstants.FCM_PREFS_NAME
import com.example.e_suratpermintaan.external.constants.PreferenceConstants.FCM_PREFS_USER_TOKENID_KEY

class FCMPreference(private val context: Context) {

    fun removeUserTokenId() {
        val prefs =
            context.getSharedPreferences(FCM_PREFS_NAME, Context.MODE_PRIVATE)
        val mEditor = prefs.edit()
        mEditor.remove(FCM_PREFS_USER_TOKENID_KEY)
        mEditor.clear()
        mEditor.apply()
    }

    fun saveUserTokenId(userTokenId: String) {
        val prefs =
            context.getSharedPreferences(FCM_PREFS_NAME, Context.MODE_PRIVATE)
        val mEditor = prefs.edit()
        mEditor.putString(FCM_PREFS_USER_TOKENID_KEY, userTokenId)
        mEditor.apply()
    }

    fun getUserTokenId(): String? {
        val prefs =
            context.getSharedPreferences(FCM_PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(FCM_PREFS_USER_TOKENID_KEY, null)
    }

}
