package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.craigmurphy.itemlog.data.local.TokenManager

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application)

    fun logout() {
        tokenManager.clearToken()
    }

    fun isLoggedIn(): Boolean {
        return !tokenManager.getToken().isNullOrBlank()
    }
}