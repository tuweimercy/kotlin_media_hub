
package com.example.mediahub.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mediahub.model.MediaItem
import com.example.mediahub.model.UserRole
import com.example.mediahub.navigation.Screen
import com.example.mediahub.ui.components.MediaPlayer
import com.example.mediahub.ui.components.openInBrowser
import com.example.mediahub.viewmodel.AuthViewModel
import com.example.mediahub.viewmodel.MediaState
import com.example.mediahub.viewmodel.MediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(navController: NavController,
                      mediaId: String,
                      authViewModel: AuthViewModel = viewModel(),
                      mediaViewModel: MediaViewModel = viewModel()
) {
    val context = LocalContext.current
    val profile by authViewModel.currentProfile.collectAsState()
    val isTeacher = profile?.userRole() == UserRole.TEACHER
    // firestore firebase references
    val publicMedia by mediaViewModel.publicMedia.collectAsState()
    val myMedia  by mediaViewModel.myMedia.collectAsState()
    val allMedia by mediaViewModel.allMedia.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()
    val isLoading = mediaState is MediaState.Loading
    //reference for a delete dialog
    var showDeleteDialog by remember { mutableStateOf(
        false) }
    // resolve the category of current item
    val item = (allMedia + myMedia + publicMedia)
        .distinctBy { it.id  }
        .find { it.id == mediaId } ?: MediaItem()
    //navigate back to listing on successful delete
    LaunchedEffect(mediaState) {
        if (mediaState is MediaState.Success) {
            mediaViewModel.clearState()
            navController.popBackStack()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack,
                            "back")
                    } },
                actions = {
                    if (isTeacher){
                        IconButton(onClick = {
                            navController.navigate(Screen.EditMedia.createRoute(
                                mediaId
                            ))
                        }) {
                            Icon(Icons.Default.Edit,
                                "Edit")
                        }
                        IconButton(
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Icon(Icons.Default.Delete,
                                "Delete")}


                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )


            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)){
            Column(modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())){
//category specific presentations
when (item.category) {
    "Image" -> {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().height(280.dp)

        )
    }
    "Video" -> {
        MediaPlayer(url = item.imageUrl, height = 220.dp)
    }
    "Audio" -> {
        MediaPlayer(url = item.imageUrl, height = 64.dp)
    }
    "Document" -> {
        Button(onClick = { openInBrowser(
            context=context,
            item.imageUrl
        ) }) {
            Text("Tap to open document")
        }
    }
    else -> {
        Icon(Icons.Default.Category,
            item.category,
            tint = MaterialTheme.colorScheme.primaryContainer,)
    }
}
          Column(Modifier.padding(20.dp)) {

Text(item.title)
                  Spacer(Modifier.height(16.dp))
Text(item.category)
                  Spacer(Modifier.height(16.dp))
Text(item.ownerName)
                  Spacer(Modifier.height(16.dp))
Text(item.description)
                  Spacer(Modifier.height(16.dp))
Text(if (item.isPublic) "PUBLIC MEDIA" else "PRIVATE MEDIA")

               }
            }
            //Loading indicator for delete process
            if(isLoading){
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }

        }

    }
    //alert dialog for delete
    if(showDeleteDialog) {
        AlertDialog(
onDismissRequest = { showDeleteDialog = false },
title = {Text("Delete Media")},
text = {Text("Delete \"${item.title}\"? This is not reversible.")},
confirmButton = {
    TextButton(
        onClick = {
            showDeleteDialog = false
            mediaViewModel.deleteMedia(item)
        }
    ) { Text("Delete",
        color = MaterialTheme.colorScheme.error)}
},
dismissButton = {
    TextButton(onClick = { showDeleteDialog = false }) {
        Text("Cancel")
    }
}
        )
    }

}