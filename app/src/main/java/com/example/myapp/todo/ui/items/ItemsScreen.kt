package com.example.myapp.todo.ui.items

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: String) -> Unit) {
    Log.d("TodoScreen", "recompose")
    val itemsViewModel = viewModel<ItemsViewModel>()
    val itemsUiState = itemsViewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.items)) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("ItemsScreen", "todo")
                },
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        ItemList(
            itemList = itemsUiState.items,
            onItemClick = onItemClick,
            modifier = Modifier.padding(it)
        )
    }
}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {})
}
