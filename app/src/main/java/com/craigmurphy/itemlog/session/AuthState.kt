package com.craigmurphy.itemlog.session

// Represents the user's current authentication state.
// Used by SessionViewModel and navigation to control access to protected screens.
sealed class AuthState {

    // User is successfully logged in and has a valid JWT token.
    object Authenticated : AuthState()

    // User is logged out or no token exists.
    object Unauthenticated : AuthState()

    // Authentication-related error state.
    // Stores an error message for display if needed.
    data class Error(val message: String) : AuthState()
}