package com.example.myapp.core

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapp.auth.data.AuthRepository
import com.example.myapp.auth.data.remote.AuthDataSource
import com.example.myapp.core.data.UserPreferencesRepository
import com.example.myapp.core.data.remote.Api
import com.example.myapp.painting_manager.data.PaintingRepository
import com.example.myapp.painting_manager.data.remote.PaintingService
import com.example.myapp.painting_manager.data.remote.PaintingWsClient

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class AppContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val paintingService: PaintingService = Api.retrofit.create(PaintingService::class.java)
    private val paintingWsClient: PaintingWsClient = PaintingWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    val paintingRepository: PaintingRepository by lazy {
        PaintingRepository(paintingService, paintingWsClient)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
}
