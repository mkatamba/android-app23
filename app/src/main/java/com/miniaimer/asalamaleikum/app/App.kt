package com.miniaimer.asalamaleikum.app

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// Declare a class named "App" that extends the "Application" class
class App : Application(){
    // Declare a companion object, which is an object that is tied to the class and 
    // can access its private members
    companion object{
        // Declare a lateinit variable named "instance" of type "App"
        lateinit var instance: App
        //Declare a lazy property named "auth" that is initialized with the
        // Firebase authentication instance
        val auth by lazy { Firebase.auth }
        // Declare a lazy property named "db" that is initialized with the 
        // Firebase Firestore instance
        val db by lazy { Firebase.firestore }
        val storage = lazy {Firebase.storage}

    }
     // Override the "onCreate" method that is called when the application is started
    override fun onCreate() {
        super.onCreate()
        instance = this  // Set the "instance" variable to the current instance of "App"
    }
}