package com.example.stocard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.stocard.ui.theme.StoCardTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoCardTheme {
                val navController = rememberNavController()
                val viewModel = viewModel<CardViewModel>()
                val cards by viewModel.cards.collectAsState()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(viewModel = viewModel, onLoginSuccess = {
                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        })
                    }

                    composable("main") {
                        MainScreen(
                            cards = cards,
                            onAddCardClick = { navController.navigate("add_card") },
                            onCardClick = { card -> navController.navigate("detail/${card.id}/${card.name}/${card.ean}") },
                            onDeleteClick = { id -> viewModel.deleteCard(id) },
                            onLogoutClick = { viewModel.logout(); navController.navigate("login") }
                        )
                    }

                    composable("add_card") {
                        AddCardScreen(onCardSaved = { name, code ->
                            viewModel.addCard(name, code)
                            navController.popBackStack()
                        })
                    }

                    // NOVÁ CESTA PRO EDITACI
                    composable(
                        route = "edit/{cardId}/{name}/{code}",
                        arguments = listOf(
                            navArgument("cardId") { type = NavType.StringType },
                            navArgument("name") { type = NavType.StringType },
                            navArgument("code") { type = NavType.StringType }
                        )
                    ) { entry ->
                        val id = entry.arguments?.getString("cardId") ?: ""
                        val name = entry.arguments?.getString("name") ?: ""
                        val code = entry.arguments?.getString("code") ?: ""

                        AddCardScreen(
                            initialName = name,
                            initialCode = code,
                            onCardSaved = { newName, newCode ->
                                viewModel.updateCard(id, newName, newCode)
                                // Vrátíme se o dva kroky zpět (z editace do seznamu, přeskočíme detail)
                                navController.popBackStack("main", inclusive = false)
                            }
                        )
                    }

                    composable(
                        route = "detail/{cardId}/{storeName}/{cardNumber}",
                        arguments = listOf(
                            navArgument("cardId") { type = NavType.StringType },
                            navArgument("storeName") { type = NavType.StringType },
                            navArgument("cardNumber") { type = NavType.StringType }
                        )
                    ) { entry ->
                        val id = entry.arguments?.getString("cardId") ?: ""
                        val name = entry.arguments?.getString("storeName") ?: ""
                        val code = entry.arguments?.getString("cardNumber") ?: ""

                        CardDetailScreen(
                            cardId = id, storeName = name, cardNumber = code,
                            onBackClick = { navController.popBackStack() },
                            onEditClick = {
                                // Navigace do editace
                                navController.navigate("edit/$id/$name/$code")
                            }
                        )
                    }
                }
            }
        }
    }
}