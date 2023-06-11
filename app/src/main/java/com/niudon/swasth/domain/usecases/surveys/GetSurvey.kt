package com.niudon.swasth.domain.usecases.surveys

import com.niudon.swasth.domain.repositories.SurveyRepository

class GetSurvey(
    private val repository: SurveyRepository
) {
    suspend operator fun invoke(
        name: String
    ) = repository.getSurvey(name)
}
