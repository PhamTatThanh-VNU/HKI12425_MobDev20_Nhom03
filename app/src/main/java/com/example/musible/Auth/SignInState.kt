package com.example.musible.Auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)