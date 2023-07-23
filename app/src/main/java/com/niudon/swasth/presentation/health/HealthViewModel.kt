package com.niudon.swasth.presentation.health

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.units.Mass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niudon.swasth.common.Constants
import com.niudon.swasth.domain.models.NDWeightLog
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.domain.usecases.weightlogs.WeightLogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import com.niudon.swasth.services.HealthConnectManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HealthViewModel @Inject constructor(
    @Named(Constants.WEIGHTLOG_USE_CASES) private var weightLogUseCases: WeightLogUseCases,
    var healthConnectManager: HealthConnectManager
) : ViewModel() {
    var weightLogsState = mutableStateOf<Response<List<NDWeightLog>>>(Response.Loading)
        private set

    var totalStepsToday = mutableStateOf<Long>(0)
        private set

    var weeklyAverageWeight = mutableStateOf<Mass?>(null)
        private set

    var dailyWeight = mutableStateOf<Mass?>(null)
        private set

    private var  weightLogsList = mutableListOf<NDWeightLog>()

    var uploadWeightLogState = mutableStateOf<Response<Void?>>(Response.Loading)
        private set

    private var permissionsGranted = mutableStateOf<Boolean>(false)

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(WeightRecord::class)
    )

    init {
        viewModelScope.launch {
            permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
            getTotalStepsToday()
            getWeightLogs()
        }
    }

    fun getTotalStepsToday() = viewModelScope.launch {
        if (permissionsGranted.value) {
            totalStepsToday.value = healthConnectManager.aggregateSteps(
                startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(),
                endTime = Instant.now()
            )
        }
    }

    /**
     * Gets the average weight over the past 7 days
     */
    fun getWeeklyAverageWeight() = viewModelScope.launch {
        val endOfWeek = Instant.now()
        val startOfWeek = endOfWeek.minus(7, ChronoUnit.DAYS)

        if (permissionsGranted.value) {
            weeklyAverageWeight.value = healthConnectManager.getAverageWeight(
                startTime = startOfWeek,
                endTime = endOfWeek
            )

            val weightRecordList = mutableListOf<Double>()

            val weightRecords = healthConnectManager.getWeightRecords(startOfWeek, endOfWeek)
            for (weightRecord in weightRecords) {
                dailyWeight.value = weightRecord.weight
                weightRecordList.add(weightRecord.weight.inGrams)
            }
        }
    }

    fun updatePermissionsStatus(granted: Boolean) {
        permissionsGranted.value = granted
    }

    fun uploadWeightLog(log: NDWeightLog): Job = viewModelScope.launch {
        weightLogUseCases.uploadWeightLog(log).collect { response ->
            uploadWeightLogState.value = response
        }
    }

    fun askForWeightLogs() = viewModelScope.launch {
        val endOfWeek = Instant.now()
        val startOfWeek = endOfWeek.minus(7, ChronoUnit.DAYS)
        healthConnectManager.getWeightRecords(startOfWeek, endOfWeek)
        val weightRecordList = mutableListOf<Double>()


        val weightRecords = healthConnectManager.getWeightRecords(startOfWeek, endOfWeek)
        for (weightRecord in weightRecords) {
            dailyWeight.value = weightRecord.weight
            //weightRecordList.add(weightRecord.weight.inGrams)
            weightLogsList.add(NDWeightLog(weight=weightRecord.weight.inGrams, epoch=weightRecord.time.toEpochMilli()))
        }
    }
    
    private fun getWeightLogs() = viewModelScope.launch {
        weightLogUseCases.getWeightLogs().collect { response ->
            weightLogsState.value = response

            if (response is Response.Success) {

                /*uploadWeightLog(
                    NDWeightLog(weight=weightRecord.weight.inGrams, epoch=weightRecord.time.toEpochMilli())
                )*/
                //fetch all data for user, store in local db, updaet as an offline sync
                response.data?.let { weightLogs ->
                    for (weightLog in weightLogs) {
                        weightLogsList.add(NDWeightLog(weight=weightLog.weight, epoch=weightLog.epoch))
                    }
                    askForWeightLogs()
                    Log.d("HealthViewModel", "weightLogs: $weightLogs")
                }
            }
        }
    }


}
