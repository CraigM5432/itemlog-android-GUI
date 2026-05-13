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

import com.craigmurphy.itemlog.ui.components.EmptyState
import com.craigmurphy.itemlog.ui.components.EventSummaryCard
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.ExportCsvViewModel

import java.io.File

// Screen responsible for CSV export and sharing.
// Retrieves transaction history as CSV text and allows secure file sharing through Android FileProvider.
@Composable
fun ExportCsvScreen(
    eventId: Long
) {
    val viewModel: ExportCsvViewModel = viewModel()
    val context = LocalContext.current

    // Loads CSV data for the selected event.
    LaunchedEffect(eventId) {
        viewModel.loadCsv(eventId)
    }

    val csvContent = viewModel.csvContent.value
    val event = viewModel.event.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    Scaffold(
        topBar = {

            // Reusable screen top bar.
            SimpleTopBar("Export CSV")
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

                // Allows CSV preview content to scroll vertically.
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.Top
        ) {

            // Displays selected event details if available.
            event?.let {

                EventSummaryCard(
                    eventName = it.eventName,
                    eventDate = it.eventDate
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Section title.
            ScreenHeader("Transactions CSV")

            Spacer(modifier = Modifier.height(16.dp))

            when {

                // Loading state.
                isLoading -> {
                    Text("Preparing CSV export...")
                }

                // Error state.
                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Empty state if there are no transactions.
                csvContent.isBlank() || csvContent.trim().lines().size <= 1 -> {
                    EmptyState(
                        title = "No export data",
                        message = "This event does not have any transaction data available to export yet.",
                        symbol = "📄"
                    )
                }

                // Displays CSV export controls and preview.
                else -> {

                    // // Creates a temporary CSV file and shares it securely using Android's FileProvider.
                    Button(
                        onClick = {

                            // Creates exports directory if it does not exist.
                            val exportsDir = File(context.filesDir, "exports")

                            if (!exportsDir.exists()) {
                                exportsDir.mkdirs()
                            }

                            // Creates CSV file inside app storage.
                            val csvFile = File(
                                exportsDir,
                                "itemlog_event_${eventId}_transactions.csv"
                            )

                            // Writes CSV text into the file.
                            csvFile.writeText(csvContent)

                            // FileProvider avoids exposing raw file system paths to external apps.
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                csvFile
                            )

                            // Android share intent used to share/export the CSV.
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {

                                // MIME type for CSV files.
                                type = "text/csv"

                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "ItemLog Transactions Export"
                                )

                                // Attaches the CSV file.
                                putExtra(Intent.EXTRA_STREAM, uri)

                                // Grants temporary read permission to other apps.
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            // Opens Android share chooser.
                            context.startActivity(
                                Intent.createChooser(shareIntent, "Share CSV")
                            )
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Share CSV File")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Displays CSV preview text directly on screen.
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