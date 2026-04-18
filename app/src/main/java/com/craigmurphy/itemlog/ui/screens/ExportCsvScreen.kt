package com.craigmurphy.itemlog.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.ExportCsvViewModel
import java.io.File

@Composable
fun ExportCsvScreen(
    eventId: Long
) {
    val viewModel: ExportCsvViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(eventId) {
        viewModel.loadCsv(eventId)
    }

    val csvContent = viewModel.csvContent.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    Scaffold(
        topBar = {
            SimpleTopBar("Export CSV")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            ScreenHeader("Transactions CSV")

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Text("Loading CSV export...")
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                csvContent.isBlank() -> {
                    Text("No CSV data available.")
                }

                else -> {
                    Button(
                        onClick = {
                            val exportsDir = File(context.filesDir, "exports")
                            if (!exportsDir.exists()) {
                                exportsDir.mkdirs()
                            }

                            val csvFile = File(exportsDir, "itemlog_event_${eventId}_transactions.csv")
                            csvFile.writeText(csvContent)

                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                csvFile
                            )

                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/csv"
                                putExtra(Intent.EXTRA_SUBJECT, "ItemLog Transactions Export")
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            context.startActivity(
                                Intent.createChooser(shareIntent, "Share CSV")
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Share CSV File")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = csvContent,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}