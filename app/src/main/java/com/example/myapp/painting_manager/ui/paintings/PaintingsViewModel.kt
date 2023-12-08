package com.example.myapp.painting_manager.ui.paintings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapp.MyApplication
import com.example.myapp.core.Result
import com.example.myapp.core.TAG
import com.example.myapp.painting_manager.data.Painting
import com.example.myapp.painting_manager.data.PaintingRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PaintingsViewModel(private val paintingRepository: PaintingRepository) : ViewModel() {
    val uiState: StateFlow<Result<List<Painting>>> = paintingRepository.paintingStream.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = Result.Loading

    )

    init {
        Log.d(TAG, "init")
        loadPaintings()
    }

    fun loadPaintings() {
        Log.d(TAG, "loadPaintings...")
        viewModelScope.launch {
            paintingRepository.refresh()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                PaintingsViewModel(app.container.paintingRepository)
            }
        }
    }
}
