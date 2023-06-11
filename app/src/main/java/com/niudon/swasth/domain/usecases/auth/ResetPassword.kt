package com.niudon.swasth.domain.usecases.auth

import com.niudon.swasth.domain.repositories.AuthRepository

class ResetPassword(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String
    ) = repository.resetPassword(email)
}
