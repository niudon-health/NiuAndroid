package com.niudon.swasth.domain.repositories

import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTaskLog
import kotlinx.coroutines.flow.Flow

interface TaskLogRepository {
    fun getTaskLogs(): Flow<Response<List<CKTaskLog>>>
    suspend fun uploadTaskLog(log: CKTaskLog): Flow<Response<Void?>>
}
