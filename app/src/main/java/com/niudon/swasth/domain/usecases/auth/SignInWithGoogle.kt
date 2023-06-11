package com.niudon.swasth.domain.usecases.auth

import com.google.firebase.auth.AuthCredential
import com.niudon.swasth.domain.repositories.AuthRepository

class SignInWithGoogle(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        googleCredential: AuthCredential
    ) = repository.firebaseSignInWithGoogle(googleCredential)
}
