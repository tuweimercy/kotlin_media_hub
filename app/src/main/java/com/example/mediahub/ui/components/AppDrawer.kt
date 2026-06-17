package com.example.mediahub.ui.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediahub.ui.theme.MediaHubTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mediahub.model.UserProfile
import com.example.mediahub.model.UserRole
@Composable
fun AppDrawer(
    profile: UserProfile?,
    currentRoute: String?,
    onDashboardClick: () -> Unit,
    onUploadClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
){
    //role references
    val isTeacher = profile?.userRole() == UserRole.TEACHER
    //container for drawer
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ){
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center

            ){
                Text(
profile?.fullName?.firstOrNull()?.uppercase() ?: "User" ,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
Text(
    if(isTeacher) "Teacher" else "Student",
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f)
)
        }
        Spacer(modifier = Modifier.height(8.dp))
        NavigationDrawerItem(
            label = { Text("Dashboard") },
            icon = { Icon(Icons.Default.Dashboard, null) },
            selected = currentRoute == "dashboard",
            onClick = onDashboardClick,
            modifier = Modifier.padding(horizontal = 12.dp)

        )
        NavigationDrawerItem(
            label = { Text("Upload Media") },
            icon = { Icon(Icons.Default.CloudUpload, null) },
            selected = currentRoute == "upload_media",
            onClick = onUploadClick,
            modifier = Modifier.padding(horizontal = 12.dp)

        )
        NavigationDrawerItem(
            label = { Text("Profile") },
            icon = { Icon(Icons.Default.Person, null) },
            selected = currentRoute == "profile",
            onClick = onProfileClick,
            modifier = Modifier.padding(horizontal = 12.dp)

        )
        Spacer(modifier = Modifier.weight(1f))
Divider(
    modifier = Modifier.padding(horizontal = 12.dp),
color = MaterialTheme.colorScheme.onSurface.copy(0.1f)
)
        NavigationDrawerItem(
        label = { Text("Logout") },
        icon = { Icon(Icons.Default.Logout, null) },
        selected = false,
        onClick = onLogoutClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
colors = NavigationDrawerItemDefaults.colors(
    unselectedTextColor = MaterialTheme.colorScheme.error,
    unselectedIconColor = MaterialTheme.colorScheme.error
)

        )


    }
}

