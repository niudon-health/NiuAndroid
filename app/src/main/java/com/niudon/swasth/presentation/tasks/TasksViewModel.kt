package com.niudon.swasth.presentation.tasks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.niudon.swasth.common.Constants
import com.niudon.swasth.common.toLocalDate
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTask
import com.niudon.swasth.domain.models.tasks.CKTaskLog
import com.niudon.swasth.domain.usecases.tasklogs.TaskLogUseCases
import com.niudon.swasth.domain.usecases.tasks.TasksUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TasksViewModel @Inject constructor(
    @Named(Constants.TASKS_USE_CASES)
    private var tasksUseCases: TasksUseCases,
    @Named(Constants.TASKLOG_USE_CASES)
    private var taskLogUseCases: TaskLogUseCases
) : ViewModel() {
    var tasksState = mutableStateOf<Response<List<CKTask>>>(Response.Loading)
        private set

    var taskLogsState = mutableStateOf<Response<List<CKTaskLog>>>(Response.Loading)
        private set

    var totalTasksToday = mutableStateOf(0)
        private set

    var totalTasksCompleteToday = mutableStateOf(0)
        private set

    // Stores the date that is active on the tasks calendar
    var currentDate = mutableStateOf<LocalDate>(LocalDate.now())
        private set

    var uploadTaskLogState = mutableStateOf<Response<Void?>>(Response.Loading)
        private set

    init {
        getTasks()
        getTaskLogs()
    }

    private fun getTasks() = viewModelScope.launch {
        tasksUseCases.getTasks().collect { response ->
            tasksState.value = response

            if (response is Response.Success) {
                response.data?.let { tasks ->
                    totalTasksToday.value = tasks.countTasksOnDate(LocalDate.now())
                }
            }
        }
    }

    private fun getTaskLogs() = viewModelScope.launch {
        taskLogUseCases.getTaskLogs().collect { response ->
            taskLogsState.value = response

            if (response is Response.Success) {
                response.data?.let { taskLogs ->
                    totalTasksCompleteToday.value = taskLogs.countTasksCompletedOnDate(LocalDate.now())
                }
            }
        }
    }

    fun uploadTaskLog(log: CKTaskLog): Job = viewModelScope.launch {
        taskLogUseCases.uploadTaskLog(log).collect { response ->
            uploadTaskLogState.value = response
        }
    }

    fun setDate(date: LocalDate) {
        currentDate.value = date
    }

    // Extensions for task metrics

    fun List<CKTask>.countTasksOnDate(date: LocalDate): Int {
        return this.count {
            it.schedule.isScheduledOn(date)
        }
    }

    fun List<CKTaskLog>.countTasksCompletedOnDate(date: LocalDate): Int {
        return this.filter {
            it.date.toLocalDate() == date
        }.distinctBy { it.taskID }.count()
    }
}
