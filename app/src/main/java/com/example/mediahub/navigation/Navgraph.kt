package com.example.mediahub.navigation
//this defines the navigation paths to our screen definations
//inside screens.kt
//allows us to write compose functions render composable elements
import androidx.compose.runtime.Composable
//this is the navigation controller
//from one screen to another through path defination
import androidx.navigation.NavHostController
//allows us to define our navigation type
//backstack:previous screen//foreground : screen in view
import androidx.navigation.NavType
//container for all our navigation screens
import androidx.navigation.compose.NavHost
//allows defination of navigation composable functions
import androidx.navigation.compose.composable
//carries path route name to different screens //navigation
import androidx.navigation.navArgument
//importing all our screens
import com.example.mediahub.ui.screens.*
@Composable
fun MediaHubNavGraph(navController: NavHostController) {
    //we define our navigation container
    //stipulate the default start destination(where does our app start)
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ){
        //state our app screens that are def. in screens.kt
        composable(route = Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(route = Screen.UploadMedia.route) {
            UploadMediaScreen(navController)
        }
        //for screens with arguments
        composable(route = Screen.MediaDetail.route,
            arguments = listOf(navArgument("mediaId")
            {type = NavType.StringType}) ){ backStack ->
            //inside we to maintain a backstack
            //i.e. when the user presses back we return to the previous screen without ID
            //pick up the mediaId from the backstack,if not present return empty string
            val mediaId = backStack.arguments?.getString("mediaId") ?: ""
            MediaDetailScreen(navController, mediaId)

        }
        composable(route = Screen.EditMedia.route,
            arguments = listOf(navArgument("mediaId")
            {type = NavType.StringType}) ){ backStack ->
            val mediaId = backStack.arguments?.getString("mediaId") ?: ""
            EditMediaScreen(navController, mediaId)

        }

    }
}