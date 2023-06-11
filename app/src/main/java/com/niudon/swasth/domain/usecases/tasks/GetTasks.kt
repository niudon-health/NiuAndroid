package com.niudon.swasth.domain.usecases.tasks

import com.niudon.swasth.domain.repositories.TasksRepository

class GetTasks(
    private val repository: TasksRepository
) {
    operator fun invoke() = repository.getTasks()
}
