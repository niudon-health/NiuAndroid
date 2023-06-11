package com.niudon.swasth.domain.repositories

import com.niudon.swasth.domain.models.Contact
import com.niudon.swasth.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(): Flow<Response<List<Contact>>>
}
