package com.niudon.swasth.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.niudon.swasth.common.Constants
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.models.NDWeightLog
import com.niudon.swasth.domain.repositories.WeightLogRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WeightLogRepositoryImpl @Inject constructor(
    @Named(Constants.WEIGHTLOG_REF)
    private val weightLogRef: CollectionReference?
) : WeightLogRepository {
    override fun getWeightLogs() = callbackFlow {
        weightLogRef?.let {
            val snapshotListener = weightLogRef.addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val weights = snapshot.toObjects(NDWeightLog::class.java)
                    Response.Success(weights)
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

    override suspend fun uploadWeightLog(log: NDWeightLog) = flow {
        weightLogRef?.let {
            try {
                emit(Response.Loading)
                val upload = weightLogRef.document().set(log).await()
                emit(Response.Success(upload))
            } catch (e: Exception) {
                emit(Response.Error(e))
            }
        }
    }
}