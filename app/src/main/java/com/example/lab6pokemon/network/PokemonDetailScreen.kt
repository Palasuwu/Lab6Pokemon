package com.uvg.lab6Pokemon.network

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.navigation.NavController
import com.example.lab6pokemon.R
import com.uvg.lab6Pokemon.network.PokemonDetail
import com.uvg.lab6Pokemon.network.PokemonSpecies
import com.uvg.lab6Pokemon.network.RetrofitClient
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(pokemonName: String, navController: NavController) {
    val pokemonDetail = remember { mutableStateOf<PokemonDetail?>(null) }
    val speciesDetail = remember { mutableStateOf<PokemonSpecies?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pokemonName) {
        coroutineScope.launch {
            val detail = RetrofitClient.apiService.getPokemonDetail(pokemonName)
            pokemonDetail.value = detail

            val species = RetrofitClient.apiService.getPokemonSpecies(pokemonName)
            speciesDetail.value = species
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(" Detalles Pokémon ") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Atras",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            pokemonDetail.value?.let { detail ->
                Text(
                    text = "Nombre: ${pokemonName.capitalize()}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                val escala = (200);
                // Mostrar las imágenes en 2 columnas y 2 filas
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Front", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.front_default),
                                contentDescription = "Front",
                                modifier = Modifier.size(escala.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Back", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.back_default),
                                contentDescription = "Back",
                                modifier = Modifier.size(escala.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Shiny Front", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.front_shiny),
                                contentDescription = "Shiny Front",
                                modifier = Modifier.size(escala.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Shiny Back", textAlign = TextAlign.Center)
                            Image(
                                painter = rememberImagePainter(detail.sprites.back_shiny),
                                contentDescription = "Shiny Back",
                                modifier = Modifier.size(escala.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                detail.types?.forEach { type ->
                    val imageRes = when (type.type.name) {
                        "grass" -> R.drawable.ic_grass
                        "fire" -> R.drawable.ic_fire // Imagen para el tipo fire
                        "water" -> R.drawable.ic_water // Imagen para el tipo water
                        "electric" -> R.drawable.ic_electric // Imagen para el tipo electric
                        "poison" -> R.drawable.ic_poison // Imagen para el tipo poison
                        else -> R.drawable.ic_unknown // Imagen de unknown para tipo voladores , hada , normal , bug y otros , ya me salio lo que queria xd
                    }

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "Type: ${type.type.name}",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Fit
                            )
                            // Texto del tipo debajo de la imagen
                            Text(
                                text = type.type.name.capitalize(),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)) //
                        }


                    }
                }
            }
        }
    }}