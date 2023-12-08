package com.example.myapp.painting_manager.data.remote

import com.example.myapp.painting_manager.data.Painting
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PaintingService {
    @GET("/api/paintings")
    suspend fun find(@Header("Authorization") authorization: String): List<Painting>

    @GET("/api/paintings/{id}")
    suspend fun read(
        @Header("Authorization") authorization: String,
        @Path("id") paintingId: String?
    ): Painting;

    @Headers("Content-Type: application/json")
    @POST("/api/paintings")
    suspend fun create(@Header("Authorization") authorization: String, @Body painting: Painting): Painting

    @Headers("Content-Type: application/json")
    @PUT("/api/paintings/{id}")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Path("id") paintingId: String?,
        @Body painting: Painting
    ): Painting
}
