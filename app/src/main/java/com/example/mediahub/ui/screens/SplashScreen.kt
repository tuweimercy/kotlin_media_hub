package com.example.mediahub.ui.screens
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
//to maintain state i.e. data inside this screen
import androidx.compose.runtime.remember
import androidx.navigation.NavController//navigation
//animation
//import androidx.compose.animation.core
//allows us to select a background effect for this screen
import androidx.compose.foundation.background
//layout configuration/alignment
import androidx.compose.foundation.layout.*
//material design imports
import androidx.compose.material3.*
//all compose runtime features
import androidx.compose.runtime.*
//ui allignments drawings and scaling (i.e. measurements)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//navigation
import androidx.navigation.compose.rememberNavController
//screens
import com.example.mediahub.navigation.Screen
import com.example.mediahub.ui.theme.MediaHubTheme
import kotlinx.coroutines.delay
//material design icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary

//timer delay

@Composable
fun SplashScreen(navController: NavController) {
       //animation effect
       val scale = remember{ Animatable( 0f) }
    //launched effect to delay splash screen showcase
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )

        )
        delay(1500)
        //after the 1.5 seconds we redirect user to the login screen
        navController.navigate(Screen.Login.route){
            //splash will become the backstack screen
            // i.e. when user presses back from login
            popUpTo(Screen.Splash.route){inclusive = true}
        }

    }
    //define our logo
    //Box :another example of a container for hosting composable elements
    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center){
        //column for vertical arrangement
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            //ICON and Text together
            Icon(
                imageVector = Icons.Default.VideoLibrary,
                contentDescription = "MediaHub Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
                    .scale(scale.value)

            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MediaHub",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = 0.6f)
            )
            Text(
                text = "Your classroom media library",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = 0.6f)
            )


        }

    }
}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    MediaHubTheme {
        SplashScreen(navController = rememberNavController())
    }
}
