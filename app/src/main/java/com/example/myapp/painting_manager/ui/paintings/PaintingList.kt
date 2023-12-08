package com.example.myapp.painting_manager.ui.paintings

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.painting_manager.data.Painting

typealias OnPaintingFn = (id: String?) -> Unit

@Composable
fun PaintingList(paintingList: List<Painting>, onPaintingClick: OnPaintingFn, modifier: Modifier) {
    Log.d("PaintingList", "recompose")
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(paintingList) { painting ->
            PaintingDetail(painting, onPaintingClick)
        }
    }
}

@Composable
fun PaintingDetail(painting: Painting, onPaintingClick: OnPaintingFn) {
//    Log.d("PaintingDetail", "recompose id = ${painting.id}")
    Row {

        ClickableText(text = AnnotatedString(painting.value.toString()),
            style = TextStyle(
                fontSize = 24.sp,
            ), onClick = { onPaintingClick(painting._id) }
        )
        Text(text = "  ")
        ClickableText(text = AnnotatedString(painting.title),
            style = TextStyle(
                fontSize = 24.sp,
            ), onClick = { onPaintingClick(painting._id) }
        )
        Text(text = "  ")
        ClickableText(text = AnnotatedString(painting.forSale.toString()),
            style = TextStyle(
                fontSize = 24.sp,
            ), onClick = { onPaintingClick(painting._id) }
        )
    }
}
