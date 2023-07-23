package com.niudon.swasth.domain.usecases.weightlogs

import com.niudon.swasth.domain.repositories.WeightLogRepository

class GetWeightLogs(
    private val repository: WeightLogRepository
) {
    operator fun invoke() = repository.getWeightLogs()
}
