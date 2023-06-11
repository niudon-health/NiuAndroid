package com.niudon.swasth.domain.usecases.surveys

import com.niudon.swasth.domain.repositories.SurveyRepository

class UploadSurveyResult(
    private val repository: SurveyRepository
) {
    suspend operator fun invoke(
        result: Map<String, Any>
    ) = repository.uploadSurveyResult(result)
}
