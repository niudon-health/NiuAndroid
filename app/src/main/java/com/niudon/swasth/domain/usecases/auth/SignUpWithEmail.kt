package com.niudon.swasth.domain.usecases.auth

import com.niudon.swasth.domain.repositories.AuthRepository

class SignUpWithEmail(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.signUp(email, password)
}
