package com.example.mediahub.ui.screens

import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mediahub.model.MediaItem
import com.example.mediahub.model.UserRole
import com.example.mediahub.navigation.Screen
import com.example.mediahub.ui.components.AppBottomBar
import com.example.mediahub.ui.components.AppDrawer
import com.example.mediahub.ui.theme.MediaHubTheme
import com.example.mediahub.viewmodel.AuthViewModel
import com.example.mediahub.viewmodel.MediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController,
                    authViewModel: AuthViewModel = viewModel(),
                    mediaViewModel: MediaViewModel = viewModel()

){
    val authState by authViewModel.authState.collectAsState()
    val profile by authViewModel.currentProfile.collectAsState()
    val isTeacher = profile?.userRole() == UserRole.TEACHER
    //call the methods for the firestore fetch
    LaunchedEffect(profile) {
        if(profile != null)
            mediaViewModel.loadPublicMedia()
        mediaViewModel.loadMyMedia()
        if (isTeacher) mediaViewModel.loadAllMedia()
    }

    // firestore firebase references
    val publicMedia by mediaViewModel.publicMedia.collectAsState()
    val myMedia  by mediaViewModel.myMedia.collectAsState()
    val allMedia by mediaViewModel.allMedia.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()
    // filtering for assets
    var selectedTab by remember { mutableStateOf(0) }
    // initial state of our drawer
    val drawerScope = rememberDrawerState(
        initialValue = DrawerValue.Closed)
    // to make updates to our drawerscope
    val scope = rememberCoroutineScope()
    // available media tabs based off role
    val tabs = if (isTeacher)
        listOf("All Media","Public","My Uploads") else
        listOf("Public","My Files")
    // tagging tab view by list position == start is 0
    val displayList = when {
        isTeacher && selectedTab == 0 -> allMedia
        isTeacher && selectedTab == 1 -> publicMedia
        isTeacher && selectedTab == 2 -> myMedia
        !isTeacher && selectedTab == 0 -> publicMedia
        else -> myMedia
    }
    ModalNavigationDrawer(
        drawerState = drawerScope,
        drawerContent = {
            AppDrawer(
                profile = profile,
                "dashboard",
                onDashboardClick = {scope.launch { drawerScope.close() }},
                onUploadClick = {scope.launch { drawerScope.close() }
                    navController.navigate(Screen.UploadMedia.route)},
                onProfileClick = {},
                onLogoutClick ={
                    scope.launch { drawerScope.close() }
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {inclusive  = true}
                    }
                }
            )
        }
    ){
        // scaffold : allows defination of diff. parts of the UI
        Scaffold(
            // top bar place click for menu to open
            topBar = {
                TopAppBar(
                    title = {Text("MediaHub")},
                    navigationIcon = { IconButton(
                        onClick = {scope.launch { drawerScope.open() }}
                    ){ Icon(Icons.Default.Menu,
                        "Menu") } },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                // bottom nav goes here
                AppBottomBar(
                    "dashboard",
                    onDashboardClick = {},
                    onUploadClick = {navController.navigate(
                        Screen.UploadMedia.route
                    )},
                    onProfileClick = {navController.navigate(
                        Screen.Profile.route
                    )}
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ){ padding ->
            Column(
                modifier=Modifier.fillMaxSize().padding(padding)
            ) {
                // Role badge .
                Row(
                    modifier=Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Default.Person,
                        null,
                        tint=MaterialTheme.colorScheme.primary,
                        modifier=Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Hi ${profile?.fullName ?: ""}." +
                                if (isTeacher) "Teacher" else "Student",
                        style=MaterialTheme.typography.labelSmall,
                        color= MaterialTheme.colorScheme.primary
                    )
                }
//tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    tabs.forEachIndexed { index, string ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {selectedTab = index},
                            text = {Text(string)}
                        )
                    }
                }
                // list of media cards
                if(displayList.isEmpty()) {
                    Box(modifier=Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        Text("No media items to display",
                            color=MaterialTheme.colorScheme.primary)
                    }
                } else {
// lazy columns display a scrollable list
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier=Modifier.fillMaxSize()
                    ){
                        items(displayList){item ->
                            MediaListCard(
                                item = item,
                                onClick = {
                                    navController.navigate(
                                        Screen.MediaDetail.createRoute(
                                            item.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
// creating a new composable for our media item card
// our card has to display the media, desc, isPublic, ,...
// to use this composable pass the media Item and a click
// functionality
@Composable
fun MediaListCard(item: MediaItem, onClick: () -> Unit){
    Card(
        modifier=Modifier.fillMaxWidth().clickable{onClick()}  ,
        shape= RoundedCornerShape(16.dp),
        colors= CardDefaults.cardColors(
            containerColor=MaterialTheme.colorScheme.surface
        ), elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier=Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //thumbnail or placeholder depending on category
            if(item.category == "image"){
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

            }
            else {
Box(contentAlignment = Alignment.Center,
    modifier=Modifier.size(72.dp).clip(RoundedCornerShape(12.dp)
    )){
    Icon(
        Icons.Default.Category,
        contentDescription = item.category,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp)
    )
}
            }


            Spacer(Modifier.width(12.dp))
            Column(modifier=Modifier.weight(1f)){
                Text(
                    item.title,
                    maxLines = 1,
                    style= MaterialTheme.typography.labelSmall,
                    color= MaterialTheme.colorScheme.onSurface,
                    overflow= TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    item.ownerName,
                    style=MaterialTheme.typography.labelSmall,
                    color= MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(Icons.Default.Category,
                        null,
                        tint=MaterialTheme.colorScheme.primary,
                        modifier=Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        item.category,
                        color= MaterialTheme.colorScheme.primary,
                        style= MaterialTheme.typography.labelSmall
                    )
                }
            }
            Surface(
                color=if(item.isPublic) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error,
                shape= RoundedCornerShape(20.dp)
            ) {
                Text(
                    if(item.isPublic) "Public" else "Private",
                    style= MaterialTheme.typography.labelSmall,
                    modifier=Modifier.padding(horizontal = 10.dp,
                        vertical = 4.dp),
                    color=MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview(){
    MediaHubTheme{
        DashboardScreen(rememberNavController())
    }
}