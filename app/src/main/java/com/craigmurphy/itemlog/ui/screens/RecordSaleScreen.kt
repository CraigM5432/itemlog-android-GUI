package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.RecordSaleViewModel

@Composable
fun RecordSaleScreen(
    eventId: Long,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    var itemId by remember { mutableStateOf("") }
    var quantitySold by remember { mutableStateOf("") }
    var salePrice by remember { mutableStateOf("") }

    val viewModel: RecordSaleViewModel = viewModel()

    Scaffold(
        topBar = {
            SimpleTopBar("Record Sale")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader("Record an item sale")

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = itemId,
                onValueChange = { itemId = it },
                label = { Text("Item ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = quantitySold,
                onValueChange = { quantitySold = it },
                label = { Text("Quantity Sold") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = salePrice,
                onValueChange = { salePrice = it },
                label = { Text("Sale Price") },
                modifier = Modifier.fillMaxWidth()
            )

            viewModel.errorMessage.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.createTransaction(
                        eventId = eventId,
                        itemId = itemId,
                        quantitySold = quantitySold,
                        salePrice = salePrice
                    ) {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoading.value) "Saving..." else "Save Sale")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    }
}