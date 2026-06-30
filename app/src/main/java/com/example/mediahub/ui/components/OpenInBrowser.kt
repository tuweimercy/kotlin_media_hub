package com.example.mediahub.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

fun openInBrowser(context: android.content.Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
