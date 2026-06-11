package com.example.mediahub.ui.screens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mediahub.navigation.Screen
import com.example.mediahub.viewmodel.AuthViewModel

@Composable
fun DashboardScreen(navController: NavController,authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.authState.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

            
            Button(onClick = {authViewModel.logout()
            navController.navigate(Screen.Login.route){
                popUpTo(Screen.Dashboard.route){inclusive = true}
            }}){
                Text("Logout")
            }
        }

}