package com.niudon.swasth.domain.repositories

import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTask
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTasks(): Flow<Response<List<CKTask>>>
}
