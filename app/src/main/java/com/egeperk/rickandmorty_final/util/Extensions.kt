package com.egeperk.rickandmorty_final.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.egeperk.rickandmorty_final.util.Constants.VIEW_DELAY
import com.google.android.material.bottomnavigation.BottomNavigationView

fun RecyclerView.bottomBarScrollState(v: BottomNavigationView) {

    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    Handler().postDelayed({
                        v.isVisible = false
                    }, VIEW_DELAY)
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    v.isVisible = true
                }
            }
        }
    })
}

fun Activity.hasInternetConnection(): Boolean {
    val connectivityManager =
        application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val hasConnection = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(hasConnection) ?: return false

    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun View.onClickAction(mLastClickTime: Long = 2000, action: () -> Unit) {
    var isEnabled = true
    this.setOnClickListener {
        if (isEnabled) {
            action()
            isEnabled = false
            postDelayed({ isEnabled = true }, mLastClickTime)
            removeCallbacks(action)
        }
    }
}
