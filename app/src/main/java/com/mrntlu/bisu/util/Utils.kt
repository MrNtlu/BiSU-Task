package com.mrntlu.bisu.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.bisu.R

fun showToast(context: Context?, message: String) = Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

fun printLog(tag: String = "Test",message:String)= Log.d(tag,message)

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    cm?.run {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }
    return result
}

// Extensions
fun View.setGone(){
    this.visibility = View.GONE
}

fun View.setVisible(){
    this.visibility = View.VISIBLE
}

fun String.getAsPureString(): String {
    return filter { it.isLetterOrDigit() }
}

fun ImageView.loadWithGlide(imageUrl:String,progressBar: ProgressBar) =
    Glide.with(context).load(imageUrl).addListener(object: RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            this@loadWithGlide.setImageResource(R.drawable.ic_error_48)
            setGone()
            progressBar.setGone()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            progressBar.setGone()
            return false
        }
    }).transform(RoundedCorners(32)).into(this)

