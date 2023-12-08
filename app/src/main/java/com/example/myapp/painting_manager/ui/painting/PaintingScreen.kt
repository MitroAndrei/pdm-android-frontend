package com.example.myapp.painting_manager.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.R
import com.example.myapp.core.Result
import com.example.myapp.painting_manager.ui.painting.PaintingViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintingScreen(paintingId: String?, onClose: () -> Unit) {
    val paintingViewModel = viewModel<PaintingViewModel>(factory = PaintingViewModel.Factory(paintingId))
    val paintingUiState = paintingViewModel.uiState

    var title by rememberSaveable { mutableStateOf(paintingUiState.painting.title) }
    var value by rememberSaveable { mutableIntStateOf(paintingUiState.painting.value) }
    var forSale by rememberSaveable { mutableStateOf(paintingUiState.painting.forSale) }
    var date by rememberSaveable { mutableStateOf(paintingUiState.painting.date) }


    Log.d("PaintingScreen", "recompose, text = $title")

    LaunchedEffect(paintingUiState.submitResult) {
        Log.d("PaintingScreen", "Submit = ${paintingUiState.submitResult}");
        if (paintingUiState.submitResult is Result.Success) {
            Log.d("PaintingScreen", "Closing screen");
            onClose();
        }
    }

    var textInitialized by remember { mutableStateOf(paintingId == null) }
    LaunchedEffect(paintingId, paintingUiState.loadResult) {
        Log.d("PaintingScreen", "Text initialized = ${paintingUiState.loadResult}");
        if (textInitialized) {
            return@LaunchedEffect
        }
        if (!(paintingUiState.loadResult is Result.Loading)) {
            title = paintingUiState.painting.title
            textInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.painting)) },
                actions = {
                    Button(onClick = {
                        Log.d("PaintingScreen", "save painting text = $title");
                        paintingViewModel.saveOrUpdatePainting(title, value, forSale, date)
                    }) { Text("Save") }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (paintingUiState.loadResult is Result.Loading) {
                CircularProgressIndicator()
                return@Scaffold
            }
            if (paintingUiState.submitResult is Result.Loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { LinearProgressIndicator() }
            }
            if (paintingUiState.loadResult is Result.Error) {
                Text(text = "Failed to load painting - ${(paintingUiState.loadResult as Result.Error).exception?.message}")
            }
            Row {
                TextField(
                    value = title,
                    onValueChange = { title = it }, label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row {
                TextField(
                    value = value.toString(),
                    onValueChange = { value = it.toInt() }, label = { Text("Value") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

//            Row {
//                TextField(
//                    value = date,
//                    onValueChange = { date = it },
//                    label = { Text("Date") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }

            Row(Modifier.selectableGroup()) {
                Text(text = "forSale")
                RadioButton(
                    selected = forSale,
                    onClick = { forSale = true },
                    modifier = Modifier.semantics { contentDescription = "Localized Description" }
                )
                Text(text = "yes")
                RadioButton(
                    selected = !forSale,
                    onClick = { forSale = false },
                    modifier = Modifier.semantics { contentDescription = "Localized Description" }
                )
                Text(text = "no")
            }

            Row {
                var millis = Instant.now().toEpochMilli();
                if(date.isNotEmpty()){
                     millis = LocalDate
                        .parse(
                            date
                        )
                        .atStartOfDay(
                            ZoneId.systemDefault()
                        )
                        .toInstant()
                        .toEpochMilli()+86400000
                }

                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = millis)
                DatePicker(
                    state = datePickerState,
                )
                val selectedDate = datePickerState.selectedDateMillis?.let {
                    Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
                }
                if(selectedDate!=null){
                    date = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                }
            }
            if (paintingUiState.submitResult is Result.Error) {
                Text(
                    text = "Failed to submit painting - ${(paintingUiState.submitResult as Result.Error).exception?.message}",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewPaintingScreen() {
    PaintingScreen(paintingId = "0", onClose = {})
}
