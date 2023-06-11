package com.niudon.swasth.domain.usecases.tasklogs

import com.niudon.swasth.domain.repositories.TaskLogRepository

class GetTaskLogs(
    private val repository: TaskLogRepository
) {
    operator fun invoke() = repository.getTaskLogs()
}
