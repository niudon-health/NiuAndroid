package com.niudon.swasth.domain.repositories

import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.NDWeightLog
import kotlinx.coroutines.flow.Flow

interface WeightLogRepository {
    fun getWeightLogs(): Flow<Response<List<NDWeightLog>>>
    suspend fun uploadWeightLog(log: NDWeightLog): Flow<Response<Void?>>
}
