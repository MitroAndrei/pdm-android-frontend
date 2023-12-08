package com.example.myapp.painting_manager.data

import android.util.Log
import com.example.myapp.core.Result
import com.example.myapp.core.TAG
import com.example.myapp.core.data.remote.Api
import com.example.myapp.painting_manager.data.remote.PaintingEvent
import com.example.myapp.painting_manager.data.remote.PaintingService
import com.example.myapp.painting_manager.data.remote.PaintingWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class PaintingRepository(private val paintingService: PaintingService, private val paintingWsClient: PaintingWsClient) {
    private var paintings: List<Painting> = listOf();

    private var paintingsFlow: MutableSharedFlow<Result<List<Painting>>> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val paintingStream: Flow<Result<List<Painting>>> = paintingsFlow

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun refresh() {
        Log.d(TAG, "refresh started")
        try {
            paintings = paintingService.find(authorization = getBearerToken())
            Log.d(TAG, "refresh succeeded")
            Log.d(TAG, paintings.size.toString())
            paintingsFlow.emit(Result.Success(paintings))
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
            paintingsFlow.emit(Result.Error(e))
        }
    }

    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getPaintingEvents().collect {
                Log.d(TAG, "Painting event collected $it")
                if (it.isSuccess) {
                    val paintingEvent = it.getOrNull();
                    when (paintingEvent?.type) {
                        "created" -> handlePaintingCreated(paintingEvent.payload)
                        "updated" -> handlePaintingUpdated(paintingEvent.payload)
                        "deleted" -> handlePaintingDeleted(paintingEvent.payload)
                    }
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            paintingWsClient.closeSocket()
        }
    }

    suspend fun getPaintingEvents(): Flow<kotlin.Result<PaintingEvent>> = callbackFlow {
        Log.d(TAG, "getPaintingEvents started")
        paintingWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent $it")
                if (it != null) {
                    trySend(kotlin.Result.success(it))
                }
            },
            onClosed = { close() },
            onFailure = { close() });
        awaitClose { paintingWsClient.closeSocket() }
    }

    suspend fun update(painting: Painting): Painting {
        Log.d(TAG, "update $painting...")
        val updatedPainting =
            paintingService.update(paintingId = painting._id, painting = painting, authorization = getBearerToken())
        Log.d(TAG, "update $painting succeeded")
        handlePaintingUpdated(updatedPainting)
        return updatedPainting
    }

    suspend fun save(painting: Painting): Painting {
        Log.d(TAG, "save $painting...")
        val createdPainting = paintingService.create(painting = painting, authorization = getBearerToken())
        Log.d(TAG, "save $createdPainting succeeded")
        //handlePaintingCreated(createdPainting)
        return createdPainting
    }

    private suspend fun handlePaintingDeleted(painting: Painting) {
        Log.d(TAG, "handlePaintingDeleted - todo $painting")
    }

    private suspend fun handlePaintingUpdated(painting: Painting) {
        Log.d(TAG, "handlePaintingUpdated...")
        paintings = paintings.map { if (it._id == painting._id) painting else it }
        paintingsFlow.emit(Result.Success(paintings))
    }

    private suspend fun handlePaintingCreated(painting: Painting) {
        Log.d(TAG, "handlePaintingCreated...")
        paintings = paintings.plus(painting)
        paintingsFlow.emit(Result.Success(paintings))
    }

//    suspend fun deleteAll() {
//        paintingDao.deleteAll()
//    }

    fun setToken(token: String) {
        paintingWsClient.authorize(token)
    }
}