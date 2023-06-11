package com.niudon.swasth.domain.repositories

import com.niudon.swasth.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    suspend fun getSurvey(name: String): Flow<Response<String?>>
    suspend fun uploadSurveyResult(result: Map<String, Any>): Flow<Response<Void?>>
}
