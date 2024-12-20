package com.niudon.swasth.domain.usecases.tasklogs

import com.google.common.truth.Truth.assertThat
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTaskLog
import com.niudon.swasth.domain.repositories.TaskLogRepository
import com.niudon.swasth.domain.usecases.tasklogs.data.repository.FakeTaskLogRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class UploadTaskLogTest {
    private lateinit var uploadTaskLog: UploadTaskLog
    private lateinit var fakeRepository: TaskLogRepository

    @Before
    fun setUp() {
        fakeRepository = FakeTaskLogRepository()
        uploadTaskLog = UploadTaskLog(fakeRepository)
    }

    @Test
    fun uploadTaskLogTest() = runBlocking {
        val log = CKTaskLog(
            taskID = "XE7VeC86W2qU9J8Q3C0A",
            date = Date()
        )

        val response = uploadTaskLog(log).first()
        assertThat(response).isInstanceOf(Response.Success::class.java)
    }
}
