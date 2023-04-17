package com.miniaimer.asalamaleikum.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
// This is a function named "isOnline" that takes a "context" of type Context as input and returns a boolean value.
class CheckOnline {
    fun isOnline(context: Context): Boolean {
        // Gets an instance of ConnectivityManager using the provided context.
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Checks if connectivityManager is not null.
        if (connectivityManager != null) {
            // Gets the network capabilities of the active network.
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            // Checks if capabilities is not null.
            if (capabilities != null) {
                // Checks if the network transport is cellular.
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                    // Checks if the network transport is wifi.
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                        // Checks if the network transport is ethernet.
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        // Returns false if the network is not available.
        return false
    }
}