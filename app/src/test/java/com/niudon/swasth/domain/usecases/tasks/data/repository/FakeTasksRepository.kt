package com.niudon.swasth.domain.usecases.tasks.data.repository

import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTask
import com.niudon.swasth.domain.repositories.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTasksRepository : TasksRepository {
    private val tasks = mutableListOf<CKTask>()

    override fun getTasks(): Flow<Response<List<CKTask>>> {
        return flow {
            emit(Response.Success(tasks))
        }
    }

    fun insertTask(task: CKTask) {
        tasks.add(task)
    }
}
