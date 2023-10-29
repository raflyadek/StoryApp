package com.example.storyapp.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.storyapp.util.Constant.MODE
import com.example.storyapp.util.Constant.PREF_NAME

object PrefManager {
    private lateinit var preferences: SharedPreferences

    private val token = Pair("token", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            PREF_NAME,
            MODE
        )
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var TOKEN: String
        get() = preferences.getString(
            token.first, token.second
        )!!
        set(value) = preferences.edit{
            it.putString(token.first, value.trim())
        }

    fun clearAllData() {
        preferences.edit()?.apply {
            remove(token.first)
            apply()
        }
    }
}
