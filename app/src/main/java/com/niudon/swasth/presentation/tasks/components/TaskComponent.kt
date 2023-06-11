package com.niudon.swasth.presentation.home.components

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.niudon.swasth.domain.models.Response
import com.niudon.swasth.presentation.common.ProgressIndicator
import com.niudon.swasth.presentation.tasks.TasksViewModel
import com.niudon.swasth.presentation.tasks.components.TaskCard
import timber.log.Timber

@Preview
@Composable
fun TaskComponent(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    when (val tasksResponse = viewModel.tasksState.value) {
        is Response.Error -> {
            Timber.d(tasksResponse.e?.message.toString())
            Toast.makeText(context, tasksResponse.e?.message, Toast.LENGTH_SHORT).show()
        }
        is Response.Loading -> ProgressIndicator()
        is Response.Success ->
            LazyColumn() {
                if (tasksResponse.data != null) {
                    items(
                        items = tasksResponse.data
                    ) { task ->
                        if (task.schedule.isScheduledOn(viewModel.currentDate.value)) {
                            TaskCard(
                                task = task
                            )
                        }
                    }
                }
            }
    }
}
