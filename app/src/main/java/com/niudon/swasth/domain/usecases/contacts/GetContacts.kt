package com.niudon.swasth.domain.usecases.contacts

import com.niudon.swasth.domain.repositories.ContactsRepository

class GetContacts(
    private val repository: ContactsRepository
) {
    operator fun invoke() = repository.getContacts()
}
