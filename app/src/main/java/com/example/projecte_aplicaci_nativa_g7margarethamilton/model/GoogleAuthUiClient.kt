package com.example.projecte_aplicaci_nativa_g7margarethamilton.model

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleAuthUiClient(
    context: Context
) {
    // Client Jaume:  114440577753-eku6mmmm7qb4pve7d8qpcubns48uhuq0.apps.googleusercontent.com
    // Client Carlos: 114440577753-h4ldc1n8svci2lll00bvhg2l431fjb1e.apps.googleusercontent.com
    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("114440577753-eku6mmmm7qb4pve7d8qpcubns48uhuq0.apps.googleusercontent.com")
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, signInOptions)

    fun getSignedInAccountFromIntent(intent: android.content.Intent): GoogleSignInAccount? {
        return GoogleSignIn.getSignedInAccountFromIntent(intent).result
    }

    fun signOut(context: Context) {
        googleSignInClient.signOut()
    }

    fun getIntent(): android.content.Intent = googleSignInClient.signInIntent
}
