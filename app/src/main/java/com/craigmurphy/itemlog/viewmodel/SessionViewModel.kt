package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.craigmurphy.itemlog.data.local.TokenManager
import com.craigmurphy.itemlog.session.AuthState

// Tracks the user's login session across the app.
// Handles logout, session restoration and expired-token behaviour.
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application)

    val authState = mutableStateOf<AuthState>(
        if (!tokenManager.getToken().isNullOrBlank()) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    )

    fun logout() {
        tokenManager.clearToken()
        authState.value = AuthState.Unauthenticated
    }

    fun isLoggedIn(): Boolean {
        return !tokenManager.getToken().isNullOrBlank()
    }

    // Called when the backend rejects a request with 401 Unauthorized.
    fun handleUnauthorized() {
        tokenManager.clearToken()
        authState.value = AuthState.Unauthenticated
    }

    fun onLoginSuccess() {
        authState.value = AuthState.Authenticated
    }
}