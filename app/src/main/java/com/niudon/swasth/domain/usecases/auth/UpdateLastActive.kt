package com.niudon.swasth.domain.usecases.auth

import com.niudon.swasth.domain.repositories.AuthRepository

class UpdateLastActive(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.updateLastActive()
}
