package com.example.scorllablegridview.utils
import android.content.Context
import androidx.core.content.edit

object SharedPreferencesHelper {
    private const val PREFS_NAME = "MyPrefs"
    private const val saveKey = "localOfflineListImages"

    fun saveData(context: Context,  value: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString(saveKey, value)
            apply()
        }
    }

    fun getData(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(saveKey, null)
    }
}