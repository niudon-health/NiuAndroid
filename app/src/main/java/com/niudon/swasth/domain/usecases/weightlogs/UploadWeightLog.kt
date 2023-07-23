package com.niudon.swasth.domain.usecases.weightlogs

import com.niudon.swasth.domain.models.NDWeightLog
import com.niudon.swasth.domain.repositories.WeightLogRepository

class UploadWeightLog (
    private val repository: WeightLogRepository
) {
    suspend operator fun invoke(log: NDWeightLog) = repository.uploadWeightLog(log)
}
