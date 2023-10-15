package com.example.storyapp.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserPreference {
    fun initPref(context: Context, name: String) : SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun editPref(context: Context, name: String) : SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveSession(context: Context, token: String) {
        val editor = editPref(context, "saveSession")
        editor.putString("token", token)
        editor.apply()
    }

    fun clearSession(context: Context) {
        val editor = editPref(context, "saveSession")
        editor.remove("token")
        editor.remove("status")
        editor.apply()
    }
}