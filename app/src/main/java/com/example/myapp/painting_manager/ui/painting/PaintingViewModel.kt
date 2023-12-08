package com.example.myapp.painting_manager.ui.painting

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

data class PaintingUiState(
    val paintingId: String? = null,
    val painting: Painting = Painting(),
    var loadResult: Result<Painting>? = null,
    var submitResult: Result<Painting>? = null,
)

class PaintingViewModel(private val paintingId: String?, private val paintingRepository: PaintingRepository) :
    ViewModel() {

    var uiState: PaintingUiState by mutableStateOf(PaintingUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (paintingId != null) {
            loadPainting()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Painting()))
        }
    }

    fun loadPainting() {
        viewModelScope.launch {
            paintingRepository.paintingStream.collect { result ->
                if (!(uiState.loadResult is Result.Loading)) {
                    return@collect
                }
                if (result is Result.Success) {
                    val paintings = result.data
                    val painting = paintings.find { it._id == paintingId } ?: Painting()
                    uiState = uiState.copy(loadResult = Result.Success(painting), painting = painting)
                } else if (result is Result.Error) {
                    uiState =
                        uiState.copy(loadResult = Result.Error(result.exception))
                }
            }
        }
    }


    fun saveOrUpdatePainting(title: String, value: Int, forSale: Boolean, date: String) {
        viewModelScope.launch {
            Log.d(TAG, "saveOrUpdatePainting...");
            try {
                uiState = uiState.copy(submitResult = Result.Loading)
                val painting = uiState.painting.copy(title = title, forSale = forSale, value = value, date = date)
                val savedPainting: Painting;
                if (paintingId == null) {
                    savedPainting = paintingRepository.save(painting)
                } else {
                    savedPainting = paintingRepository.update(painting)
                }
                Log.d(TAG, "saveOrUpdatePainting succeeded");
                uiState = uiState.copy(submitResult = Result.Success(savedPainting))
            } catch (e: Exception) {
                Log.d(TAG, "saveOrUpdatePainting failed");
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }

    companion object {
        fun Factory(paintingId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                PaintingViewModel(paintingId, app.container.paintingRepository)
            }
        }
    }
}
