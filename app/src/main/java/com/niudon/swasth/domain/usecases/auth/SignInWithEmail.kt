package com.niudon.swasth.domain.usecases.auth

import com.niudon.swasth.domain.repositories.AuthRepository

class SignInWithEmail(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.signIn(email, password)
}
