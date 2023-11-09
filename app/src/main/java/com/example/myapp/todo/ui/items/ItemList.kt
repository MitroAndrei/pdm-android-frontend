package com.example.myapp.todo.ui.items

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.myapp.todo.data.Item

typealias OnItemFn = (id: String) -> Unit

@Composable
fun ItemList(itemList: List<Item>, onItemClick: OnItemFn, modifier: Modifier) {
    Log.d("ItemList", "recompose")
    LazyColumn(modifier = modifier) {
        items(itemList) { item ->
            ItemDetail(item, onItemClick)
        }
    }
}

@Composable
fun ItemDetail(item: Item, onItemClick: OnItemFn) {
    Log.d("ItemDetail", "recompose id = ${item.id}")
    Row {
        ClickableText(text = AnnotatedString(item.text),
            style = TextStyle(
                fontSize = 24.sp,
            ), onClick = { onItemClick(item.id) })
    }
}
