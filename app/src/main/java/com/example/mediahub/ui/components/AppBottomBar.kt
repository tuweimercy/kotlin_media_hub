package com.example.mediahub.ui.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediahub.ui.theme.MediaHubTheme
@Composable
fun AppBottomBar(
    currentRoute: String,
    onDashboardClick: () -> Unit,
    onUploadClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    // host - navigation bar
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,

    ) {
        // here we now house our nav items / links
        // use navgraph route names for the links
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = onDashboardClick,
            icon = {
                Icon(
                   Icons.Default.Dashboard,
                     "Dashboard"
                )
            },
            label = {
                Text(text = "Dashboard")
            }


            )
        NavigationBarItem(
            selected = currentRoute == "upload_media",
            onClick = onUploadClick,
            icon = {
                Icon(
                    Icons.Default.CloudUpload,
                    "Upload"
                )
            },
            label = {
                Text(text = "Upload Media")
            }


        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = onProfileClick,
            icon = {
                Icon(
                    Icons.Default.Person,
                    "Profile"
                )
            },
            label = {
                Text(text = "Profile")
            }


        )
    }
}
