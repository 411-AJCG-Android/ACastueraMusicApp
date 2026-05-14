package com.example.musicapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.musicapp.components.AlbumItem
import com.example.musicapp.components.MiniPlayer
import com.example.musicapp.models.Album
import com.example.musicapp.services.AlbumsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun DetailScreen(id: String, onBack: () -> Unit = {}) {
    val BASE_URL = "https://musicapi.pjasoft.com/"
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val result = async(Dispatchers.IO) {
                val service = retrofit.create(AlbumsService::class.java)
                service.getAlbumById(id)
            }
            album = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("DetailScreen", "Error cargando álbum: ${e.message}")
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEBE6F9))
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF673AB7))
        } else if (album != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 130.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 40.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(380.dp),
                            shape = RoundedCornerShape(32.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = album!!.image,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color(0xFF2A0C4A).copy(alpha = 0.6f),
                                                    Color(0xFF2A0C4A).copy(alpha = 0.95f)
                                                ),
                                                startY = 300f
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = album!!.title,
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = album!!.artist,
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF7C4DFF),
                                            modifier = Modifier.size(56.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(24.dp))
                                            }
                                        }
                                        Surface(
                                            shape = CircleShape,
                                            color = Color.White,
                                            modifier = Modifier.size(56.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF230C4F), modifier = Modifier.size(24.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.4f),
                                modifier = Modifier.size(40.dp).clickable { onBack() }
                            ) {
                                Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.4f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(Icons.Default.FavoriteBorder, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 25.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "About this album",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF230C4F)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = album!!.description,
                                color = Color.Gray,
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(25.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(25.dp))
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Artist: ", fontWeight = FontWeight.Bold, color = Color(0xFF230C4F))
                        Text(text = album!!.artist, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(10) { index ->
                    AlbumItem (
                        albumImage = album!!.image,
                        title = "${album!!.title} • Track ${index + 1}",
                        artist = album!!.artist,
                        onClick = { }
                    )
                }
            }
            MiniPlayer(album = album!!)
        }
    }
}