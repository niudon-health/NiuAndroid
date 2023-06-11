package com.niudon.swasth.domain.usecases.auth

import com.niudon.swasth.domain.repositories.AuthRepository

class GetAuthStatus(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getAuthStatus()
}
