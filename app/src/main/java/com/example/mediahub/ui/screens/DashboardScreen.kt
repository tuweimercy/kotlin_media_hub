package com.example.mediahub.ui.screens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mediahub.model.UserRole
import com.example.mediahub.navigation.Screen
import com.example.mediahub.ui.components.AppDrawer
import com.example.mediahub.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController,authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.authState.collectAsState()
    val profile by authViewModel.currentProfile.collectAsState()
    val isTeacher = profile?.userRole() == UserRole.TEACHER
    // initial state of our drawer
    val drawerScope = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    //to make updates to our drawerscope
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerScope,
        drawerContent = {
            AppDrawer(
                profile = profile,
                "dashboard",
onDashboardClick = {scope.launch { drawerScope.close() }},
onUploadClick = {scope.launch { drawerScope.close()}
navController.navigate(Screen.UploadMedia.route)},
onProfileClick = {},
onLogoutClick = {
    scope.launch { drawerScope.close()}
    authViewModel.logout()
    navController.navigate(Screen.Login.route) {
        popUpTo(0){inclusive = true}
    }
}
            ){
//scaffold :allows defination of diff. parts of the UI
                Scaffold(
topBar = {},
bottomBar = {},
containerColor = MaterialTheme.colorScheme.background
                ){}
            }
        }
    ) { }


}