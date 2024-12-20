package com.niudon.swasth.domain.usecases.tasks

import com.google.common.truth.Truth.assertThat
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTask
import com.niudon.swasth.domain.models.tasks.CKTaskCategory
import com.niudon.swasth.domain.models.tasks.CKTaskContext
import com.niudon.swasth.domain.models.tasks.CKTaskSchedule
import com.niudon.swasth.domain.repositories.TasksRepository
import com.niudon.swasth.domain.usecases.tasks.data.repository.FakeTasksRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class GetTasksTest {
    private lateinit var getTasks: GetTasks
    private lateinit var fakeRepository: TasksRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTasksRepository()
        getTasks = GetTasks(fakeRepository)

        val tasksToInsert = mutableListOf<CKTask>()

        val fakeTask0 = CKTask(
            id = "XE7VeC86W2qU9J8Q3C0A",
            title = "First Survey",
            description = "This is the first survey.",
            context = CKTaskContext(
                category = CKTaskCategory.SURVEY,
                uri = "first_survey.json"
            ),
            schedule = CKTaskSchedule(
                startDate = Date(),
                endDate = null,
                interval = 1,
                description = "taken daily"
            ),
            isActive = true
        )

        val fakeTask1 = CKTask(
            id = "sIbsVjdnpkcaC5tjJwjQ",
            title = "Second Survey",
            description = "This is the second survey.",
            context = CKTaskContext(
                category = CKTaskCategory.SURVEY,
                uri = "second_survey.json"
            ),
            schedule = CKTaskSchedule(
                startDate = Date(),
                endDate = null,
                interval = 1,
                description = "taken daily"
            ),
            isActive = false
        )

        tasksToInsert.add(fakeTask0)
        tasksToInsert.add(fakeTask1)

        runBlocking {
            tasksToInsert.forEach { (fakeRepository as FakeTasksRepository).insertTask(it) }
        }
    }

    @Test
    fun getTasksTest() = runBlocking {
        val response = getTasks().first()
        var tasks = listOf<CKTask>()

        if (response is Response.Success && response.data != null) {
            tasks = response.data as List<CKTask>
        }

        assertThat(tasks[0].id).isEqualTo("XE7VeC86W2qU9J8Q3C0A")
        assertThat(tasks[1].id).isEqualTo("sIbsVjdnpkcaC5tjJwjQ")
    }
}
