package com.example.mediahub.ui.components
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mediahub.ui.theme.MediaHubTheme
@Composable
fun AppBottomBar(
    currentRoute: String,
    onDashboardClick: () -> Unit,
    onUploadClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    //define a reference  for our bottom nav background color
    val barBlue = Color(0xFF1565C0)
    val red = Color.Red
    // host - navigation bar
    NavigationBar(
        containerColor = barBlue,
        //modifier = Modifier.height(80.dp)//height for bottom nav

        ) {
        //itemcolors = showcase for when items are clicked
val itemColors = NavigationBarItemDefaults.colors(
    indicatorColor = Color.White,
    selectedIconColor = Color.White,
    selectedTextColor = Color.White,
    unselectedIconColor = Color.Gray,
    unselectedTextColor = Color.Gray,
)
        // here we now house our nav items / links
        // use navgraph route names for the links
        NavigationBarItem(
            colors = itemColors,
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
            colors = itemColors,
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
            colors = itemColors,
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



