package com.example.mediahub

//application classes control access to external services
//by creating a central initialization point
import android.app.Application
import com.cloudinary.android.MediaManager
class MediaHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //config cloudinary by pointing to cloudname
        val config = mapOf(
            "cloud_name" to "divls8aj2" //get cloud name from cloudinary
        )
        MediaManager.init(this, config)

    }
}