package com.example.musicapp.models

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val title: String,
    val artist: String,
    val description: String,
    val image: String,
    val id: String
)