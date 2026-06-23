
package com.example.mediahub.utils
//context allows us to access services within screens
import android.content.Context
//Uri : this will allow us to generate / receive URL addresses
import android.net.Uri
//cloudinary classes
//MediaManager : provides methods for uploads
import com.cloudinary.android.MediaManager
//error info :logs any error related to cloudinary process
import com.cloudinary.android.callback.ErrorInfo
//uploadcallback :tags responses from cloudinary upload process
import com.cloudinary.android.callback.UploadCallback
//Background processing i.e. when user upload items do not stop other tasks-coroutines
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
object CloudinaryUploader {
    //Suspend function is a function that delays up until it is to be used
    suspend fun uploadImage(
        context: Context,
        imageUri: Uri, //image path uploaded
        preset: String = "mediahub_preset", //folder from cloudinary
        onProgress: (Float) -> Unit = {}
    ): String = suspendCoroutine { continuation ->
        // get the media path that user want to upload
        //set up the methods to show progress ,error or interruptions
        MediaManager.get()
            .upload(imageUri)
            .unsigned(preset)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    onProgress(bytes.toFloat() / totalBytes.toFloat())
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    // after upload cloudinary returns a url
                    //to the access the mediaItem
                    val url = resultData?.get("url") as? String
                    if (url != null) {
                        continuation.resume(url)
                    } else {
                        continuation.resumeWithException(Exception("No URL returned"))
                    }

                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description))
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception(error?.description))
                }

            }).dispatch(context)
    }
}









