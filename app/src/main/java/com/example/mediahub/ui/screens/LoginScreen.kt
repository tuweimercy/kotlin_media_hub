package com.example.mediahub.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
@Composable
fun LoginScreen(navController: NavController) {
    Box(){
        Column(){
            Text(text = "This is the Login Screen")
        }
    }
}