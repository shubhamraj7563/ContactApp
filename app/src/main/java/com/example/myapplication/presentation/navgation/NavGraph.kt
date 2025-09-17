package com.example.myapplication.presentation.navgation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.presentation.ContactViewModel
import com.example.myapplication.presentation.Screen.AddEditScreen
import com.example.myapplication.presentation.Screen.HomeScreen

@Composable
fun NavGraph (navController: NavHostController,viewModel: ContactViewModel){
    val state by viewModel.state.collectAsState()
    NavHost(navController = navController, startDestination = Routes.Home.route)


    {
        composable(route = Routes.AddEdit.route){
            AddEditScreen(
                state = state, // Pass the collected state directly
                onEvent  = { viewModel.saveContact() }, // Pass as a lambda
                navController = navController
                // Removed 'it' parameter as it's handled internally by AddEditScreen
            )
        }
        composable(route = Routes.Home.route){
            HomeScreen(
              navController = navController,
                state= state,
                viewModel = viewModel
            )

        }

    }

}