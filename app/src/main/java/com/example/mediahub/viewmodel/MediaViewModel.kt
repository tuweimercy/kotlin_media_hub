package com.example.mediahub.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.mediahub.model.MediaItem
import com.example.mediahub.model.UserProfile
import com.example.mediahub.utils.CloudinaryUploader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID // generate random ids

sealed class MediaState {
    object Idle : MediaState()
    object Loading : MediaState()
    object Success : MediaState()
    data class Error(val message: String) : MediaState()
}

class MediaViewModel : ViewModel() {
    // references variables for processes
    //we need to know our logged in user
    private val auth = FirebaseAuth.getInstance()

    //initilize firestore
    private val db = FirebaseFirestore.getInstance()

    //access to public media items in view model
    private val _publicMedia = MutableStateFlow<List<MediaItem>>(emptyList())

    //access to public media items in screens using this vm
    val publicMedia: StateFlow<List<MediaItem>> = _publicMedia

    private val _myMedia = MutableStateFlow<List<MediaItem>>(emptyList())

    //access to private media items in screens using this vm
    val myMedia: StateFlow<List<MediaItem>> = _myMedia

    private val _allMedia = MutableStateFlow<List<MediaItem>>(emptyList())

    //teachers
    //access to public media items in screens using this vm
    val allMedia: StateFlow<List<MediaItem>> = _allMedia

    //status check
    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Idle)
    val mediaState: StateFlow<MediaState> = _mediaState

    //progress indication
    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress

    // load public media items
    fun loadPublicMedia() {
        viewModelScope.launch {
            try {
                //first we retrieve the firestore collection
                //filter the data via the isPublic field = true
                //order our data by the latest i.e. uploadAt field
                val snapshot = db.collection("media")
                    .whereEqualTo("isPublic", true)
                    .orderBy("uploadedAt", Query.Direction.DESCENDING)
                    .get().await()
                //now populate the viewmodel reference for public media via capturing the snapshot
                // and mapping each record in the collection to a media item
                _publicMedia.value = snapshot.documents.map { doc ->
                    doc.toObject(MediaItem::class.java)!!.copy(
                        id = doc.id
                    )
                }


            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Failed to load media")
            }
        }
    }

    //load users private media items
    fun loadMyMedia() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = db.collection("media")
                    .whereEqualTo("ownerId", uid)
                    .orderBy("uploadedAt", Query.Direction.DESCENDING)
                    .get().await()
                _myMedia.value = snapshot.documents.map { doc ->
                    doc.toObject(MediaItem::class.java)!!.copy(
                        id = doc.id
                    )
                }

            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Failed to load media")
            }
        }
    }

    // load all media for teachers access /view
    fun loadAllMedia() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("media")
                    .orderBy("uploadedAt", Query.Direction.DESCENDING)
                    .get().await()
                _allMedia.value = snapshot.documents.map { doc ->
                    doc.toObject(MediaItem::class.java)!!.copy(
                        id = doc.id
                    )
                }

            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Failed to load media")
            }
        }

    }

    //upload new media item
    fun uploadMedia(
        context: Context,
        title: String,
        description: String,
        category: String,
        isPublic: Boolean,
        mediaUri: Uri, // media item path from storage
        ownerName: String,
    ) {
        //we tag logged in user via their id
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                //1. upload to cloudinary and get acess url
                val mediaUrl = CloudinaryUploader.uploadImage(
                    context = context,
                    imageUri = mediaUri,
                    onProgress = { progress ->
                        _uploadProgress.value = progress
                    }
                )
                //2. save media asset to firestore with correct url
                val mediaItem = MediaItem(
                    title = title,
                    description = description,
                    imageUrl = mediaUrl,
                    ownerName = ownerName,
                    ownerId = uid,
                    category = category,
                    isPublic = isPublic
                )
                db.collection("media").add(mediaItem.toMap()).await()
                //change the progress value
                _uploadProgress.value = 0f
                _mediaState.value = MediaState.Success

            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Upload Failed")
            }

        }
    }

    // update existing media
    fun updateMedia(
        mediaId: String,
        title: String,
        description: String,
        isPublic: Boolean,
    ) {
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                db.collection("media").document(mediaId).update(
                    mapOf(
                        "title" to title,
                        "description" to description,
                        "isPublic" to isPublic
                    )
                ).await()
                _mediaState.value = MediaState.Success

            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Update Failed")
            }
        }
    }

    // delete media
    fun deleteMedia(item: MediaItem) {
        viewModelScope.launch {
            try {
                db.collection("media").document(item.id).delete().await()
                _mediaState.value = MediaState.Success
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Delete Failed!!")
            }
        }
    }

    //clearstate
    fun clearState() {
        _mediaState.value = MediaState.Idle

    }

}
