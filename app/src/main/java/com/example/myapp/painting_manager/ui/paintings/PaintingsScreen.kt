package com.example.myapp.painting_manager.ui.paintings

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.R
import com.example.myapp.core.Result
import com.example.myapp.painting_manager.data.Painting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintingsScreen(onPaintingClick: (id: String?) -> Unit, onAddPainting: () -> Unit, onLogout: () -> Unit) {
    Log.d("PaintingsScreen", "recompose")
    val paintingsViewModel = viewModel<PaintingsViewModel>(factory = PaintingsViewModel.Factory)
    val paintingsUiState by paintingsViewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.paintings)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("PaintingsScreen", "add")
                    onAddPainting()
                },
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        when (paintingsUiState) {
            is Result.Success ->
                PaintingList(
                    paintingList = (paintingsUiState as Result.Success<List<Painting>>).data,
                    onPaintingClick = onPaintingClick,
                    modifier = Modifier.padding(it)
                )

            is Result.Loading -> CircularProgressIndicator(modifier = Modifier.padding(it))
            is Result.Error -> Text(
                text = "Failed to load paintings - ${(paintingsUiState as Result.Error).exception?.message}",
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Preview
@Composable
fun PreviewPaintingsScreen() {
    PaintingsScreen(onPaintingClick = {}, onAddPainting = {}, onLogout = {})
}
