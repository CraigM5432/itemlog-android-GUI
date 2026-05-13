package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.EditItemViewModel
import java.net.URLDecoder

// Screen for editing an existing merchandise item.
// Existing item values are passed through navigation arguments.
@Composable
fun EditItemScreen(
    eventId: Long,
    itemId: Long,
    initialName: String,
    initialPrice: String,
    initialSize: String,
    initialQuantity: String,
    initialDescription: String,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    // Initial values are passed through navigation route arguments.
    // URLDecoder converts encoded text back to normal text.
    var itemName by remember { mutableStateOf(URLDecoder.decode(initialName, "UTF-8")) }
    var price by remember { mutableStateOf(initialPrice) }
    var quantity by remember { mutableStateOf(initialQuantity) }
    var size by remember { mutableStateOf(URLDecoder.decode(initialSize, "UTF-8")) }
    var description by remember { mutableStateOf(URLDecoder.decode(initialDescription, "UTF-8")) }

    // ViewModel handles validation and update API call.
    val viewModel: EditItemViewModel = viewModel()

    Scaffold(
        topBar = {
            SimpleTopBar("Edit Item")
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
            ScreenHeader("Update item details")

            Spacer(modifier = Modifier.height(24.dp))

            // Item name input.
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price input.
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity input.
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Size input.
            OutlinedTextField(
                value = size,
                onValueChange = { size = it },
                label = { Text("Size") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description input.
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            // Displays validation/API errors.
            viewModel.errorMessage.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Saves item changes.
            Button(
                onClick = {
                    viewModel.updateItem(
                        eventId = eventId,
                        itemId = itemId,
                        name = itemName,
                        price = price,
                        size = size,
                        quantity = quantity,
                        description = description
                    ) {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoading.value) "Saving..." else "Save Changes")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cancels and returns to previous screen.
            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    }
}