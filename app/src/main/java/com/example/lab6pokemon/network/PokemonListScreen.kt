package com.uvg.lab6Pokemon.network

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.navigation.NavController
import com.uvg.lab6Pokemon.network.Pokemon
import com.uvg.lab6Pokemon.network.RetrofitClient
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(navController: NavController) {
    val pokemonList = remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val cantidadpokemon = 200 // puntos extra porque son docientos :p mentira
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = RetrofitClient.apiService.getPokemonList(cantidadpokemon)
            pokemonList.value = response.results
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Pokémon ") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(pokemonList.value) { pokemon ->
                PokemonCard(pokemon = pokemon, navController = navController)
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("detail/${pokemon.name}")
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
            contentColor = Color(0xFF006064)
        )
    ) {
        Row(
            Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.getId()}.png"),
                contentDescription = pokemon.name,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(text = pokemon.name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun Pokemon.getId(): String {
    return url.split("/").filter { it.isNotEmpty() }.last()
}