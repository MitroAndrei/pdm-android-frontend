package com.example.myapp.todo.data.remote

import com.example.myapp.todo.data.Item
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ItemService {
    @GET("/api/paintings")
    suspend fun find(@Header("Authorization") authorization: String): List<Item>

    @GET("/api/paintings/{id}")
    suspend fun read(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: String?
    ): Item;

    @Headers("Content-Type: application/json")
    @POST("/api/paintings")
    suspend fun create(@Header("Authorization") authorization: String, @Body item: Item): Item

    @Headers("Content-Type: application/json")
    @PUT("/api/paintings/{id}")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: String?,
        @Body item: Item
    ): Item
}
