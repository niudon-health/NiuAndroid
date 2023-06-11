package com.niudon.swasth.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.niudon.swasth.common.Constants
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.tasks.CKTaskLog
import com.niudon.swasth.domain.repositories.TaskLogRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TaskLogRepositoryImpl @Inject constructor(
    @Named(Constants.TASKLOG_REF)
    private val taskLogRef: CollectionReference?
) : TaskLogRepository {
    override fun getTaskLogs() = callbackFlow {
        taskLogRef?.let {
            val snapshotListener = taskLogRef.addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val tasks = snapshot.toObjects(CKTaskLog::class.java)
                    Response.Success(tasks)
                } else {
                    Response.Error(e)
                }
                trySend(response).isSuccess
            }
            awaitClose {
                snapshotListener.remove()
            }
        }
    }

    override suspend fun uploadTaskLog(log: CKTaskLog) = flow {
        taskLogRef?.let {
            try {
                emit(Response.Loading)
                val upload = taskLogRef.document().set(log).await()
                emit(Response.Success(upload))
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }
    }
}
