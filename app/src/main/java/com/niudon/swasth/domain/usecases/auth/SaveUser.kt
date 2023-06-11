package com.niudon.swasth.domain.usecases.auth

import com.niudon.swasth.domain.repositories.AuthRepository

class SaveUser(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.saveUser()
}
