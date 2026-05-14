package com.example.musicapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
fun HomeScreen(
    navController: NavController = rememberNavController()
) {
    val BASE_URL = "https://musicapi.pjasoft.com/"
    var albums by remember { mutableStateOf(listOf<Album>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async(Dispatchers.IO) {
                val service = retrofit.create(AlbumsService::class.java)
                service.getAllAlbums()
            }

            albums = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            isLoading = false
        }
    }
    val screenBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDED4F5),
            Color.White
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackgroundGradient)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 110.dp)
                ) {
                    item {
                        val headerGradient = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFA37DFF),
                                Color(0xFF8F5CFF)
                            )
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .padding(top = 32.dp),
                            shape = RoundedCornerShape(35.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(headerGradient)
                                    .padding(24.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(Icons.Default.Menu, null, tint = Color.White)
                                    Icon(Icons.Default.Search, null, tint = Color.White)
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    "Good Morning!",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 16.sp
                                )
                                Text(
                                    "Astrid Castuera",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Albums", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "See more",
                                color = Color(0xFF8B5CF6),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {  }
                            )
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(albums) { album ->
                                Card(
                                    modifier = Modifier
                                        .width(210.dp)
                                        .height(210.dp)
                                        .clickable { navController.navigate("detail/${album.id}") },
                                    shape = RoundedCornerShape(32.dp)
                                ) {
                                    Box {
                                        AsyncImage(
                                            model = album.image,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 12.dp)
                                                .height(85.dp)
                                                .clip(RoundedCornerShape(26.dp))
                                                .background(Color(0xFF1F1235).copy(alpha = 0.7f))
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        album.title,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        fontSize = 18.sp,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        album.artist,
                                                        color = Color.White.copy(alpha = 0.6f),
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Surface(
                                                    modifier = Modifier.size(42.dp),
                                                    shape = CircleShape,
                                                    color = Color.White
                                                ) {
                                                    Icon(
                                                        Icons.Default.PlayArrow, null,
                                                        tint = Color(0xFF1F1235),
                                                        modifier = Modifier.padding(6.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Recently Played", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "See more",
                                color = Color(0xFF8B5CF6),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {  }
                            )
                        }
                    }

                    items(albums) { album ->
                        AlbumItem(
                            albumImage = album.image,
                            title = album.title,
                            artist = "${album.artist} • Popular Song",
                            onClick = { navController.navigate("detail/${album.id}") }
                        )
                    }
                }
            }

            if (albums.isNotEmpty()) {
                MiniPlayer(album = albums[0] )
            }
        }
    }
}