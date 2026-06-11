package com.example.mediahub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mediahub.navigation.Screen
import com.example.mediahub.ui.theme.MediaHubTheme
import com.example.mediahub.viewmodel.AuthState
import com.example.mediahub.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController,
                authViewModel: AuthViewModel = viewModel()
 ) {
    //data to be maintained in the screen
    //login info: email x password
    //mutable state of= data that can change
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    //controlling visibility of password showcase
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading
    val errorMessage = (authState as? AuthState.Error)?.message
    // when a user registers successfully we take them to the dashboard
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,){
            Icon(Icons.Default.VideoLibrary,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size( 64.dp))
            Spacer(Modifier.height(8.dp))
            Text(text = "Welcome Back",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(8.dp))
            Text(text = "Sign in to MediaHub",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            Spacer(Modifier.height(40.dp))
            //form inputs
            //onValuechhange = captures the current input the user enters
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email

                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {Icon(Icons.Default.Lock, contentDescription = null)},
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
            Spacer(Modifier.height(8.dp))
            // link to forgot password screen
            TextButton(
                onClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary)

            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    authViewModel.Login(email, password)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = email.isNotBlank() && password.isNotBlank() && !isLoading
            ) {
                if(isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                }
                else {
                    Text(text = "Sign in",style = MaterialTheme.typography.bodyLarge)
                }
            }

//            Button(
//                onClick = {},
//                modifier = Modifier.fillMaxWidth().height(52.dp),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text(text = "Sign in",
//                    style = MaterialTheme.typography.bodyLarge)
//            }
            Spacer(Modifier.height(16.dp))
            //to link to the register screen
            TextButton(
                onClick = {
                    authViewModel.clearState()
                    navController.navigate(Screen.Register.route)
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = "Don't have an account? Register",
                    color = MaterialTheme.colorScheme.primary)

            }


        }
    }
}
@Preview
@Composable
fun LoginScreenPreview() {
    MediaHubTheme {
        LoginScreen(navController = rememberNavController())
    }

}