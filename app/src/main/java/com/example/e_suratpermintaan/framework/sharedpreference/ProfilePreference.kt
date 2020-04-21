package com.example.e_suratpermintaan.framework.sharedpreference

import android.content.Context
import com.e_suratpermintaan.core.domain.entities.responses.data_response.DataProfile
import com.example.e_suratpermintaan.external.constants.PreferenceConstants.PROFILE_PREFS_NAME
import com.example.e_suratpermintaan.external.constants.PreferenceConstants.PROFILE_PREFS_STRING_KEY
import com.google.gson.Gson

class ProfilePreference(private val context: Context) {

    fun removeProfile() {
        val prefs =
            context.getSharedPreferences(PROFILE_PREFS_NAME, Context.MODE_PRIVATE)
        val mEditor = prefs.edit()
        mEditor!!.remove(PROFILE_PREFS_STRING_KEY)
        mEditor.apply()
    }

    fun saveProfile(profile: DataProfile?) {
        val prefs =
            context.getSharedPreferences(PROFILE_PREFS_NAME, Context.MODE_PRIVATE)
        val mEditor = prefs!!.edit()
        val gson = Gson()
        val profileJson = gson.toJson(profile)
        mEditor!!.putString(PROFILE_PREFS_STRING_KEY, profileJson)
        mEditor.apply()
    }

    fun getProfile(): DataProfile? {
        val prefs =
            context.getSharedPreferences(PROFILE_PREFS_NAME, Context.MODE_PRIVATE)
        val profileJson = prefs!!.getString(PROFILE_PREFS_STRING_KEY, null)
        val gson = Gson()
        return gson.fromJson(profileJson, DataProfile::class.java)
    }

}
