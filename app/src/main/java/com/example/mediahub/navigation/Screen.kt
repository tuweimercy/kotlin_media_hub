package com.example.mediahub.navigation
//this defines paths to access our composable screens
// sealed clas : means it cannot be inherited (OOP)
sealed class Screen(val route: String){
    //inside define screen together with access path
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Dashboard : Screen("dashboard")
    object MediaDetail : Screen("media_detail/{mediaId}")
    {
        fun createRoute(mediaId: String)="media_detail/$mediaId"
    }
    object UploadMedia : Screen("upload_media")
    object EditMedia : Screen("edit_media/{mediaId}")
    {
        fun createRoute(mediaId: String)="edit_media/$mediaId"
    }
    object Profile : Screen("profile")
}