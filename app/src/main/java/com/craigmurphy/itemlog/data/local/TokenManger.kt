package com.craigmurphy.itemlog.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }
}