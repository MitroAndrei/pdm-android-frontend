package com.example.myapp.painting_manager.data.remote

import com.example.myapp.painting_manager.data.Painting

data class PaintingEvent(val type: String, val payload: Painting)
