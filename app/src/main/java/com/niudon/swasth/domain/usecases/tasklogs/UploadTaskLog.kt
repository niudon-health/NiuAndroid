package com.niudon.swasth.domain.usecases.tasklogs

import com.niudon.swasth.domain.models.tasks.CKTaskLog
import com.niudon.swasth.domain.repositories.TaskLogRepository

class UploadTaskLog(
    private val repository: TaskLogRepository
) {
    suspend operator fun invoke(log: CKTaskLog) = repository.uploadTaskLog(log)
}
